import time
import math
import os
import keyboard

class node_3d:
    def __init__(self):
        self.pos = [0,0,0]
        self.vel = [0,0,0]
        self.mass = 1

        self.forces = []
    
    def physics_step(self, delta):
        for force in self.forces:
            self.handle_force(force, delta)
        self.pos = [self.pos[x] + delta*self.vel[x] for x in range(3)]
    
    def handle_force(self, force, delta):
        # F=ma
        # F=m * (Δv/Δt)
        # F/m = Δv/Δt
        # Δv = F/m * Δt
        delta_v = [force[i]/(0.0001 if self.mass==0 else self.mass) * delta for i in range(3)]
        #self.vel = [delta_v[x]*]


class game_tree:
    def __init__(self):
        self.tree = []

        self.finished = False
    
    def main(self):
        prev_time = time.time()
        while True:
            if self.finished or keyboard.is_pressed("esc"):
                break

            delta = time.time() - prev_time
            prev_time = time.time()
            for node in self.tree:
                if isinstance(node, camera):
                    node.render(self)
                    node.camera_control(delta)
                    print(f"Python FPS: {1/delta}")
                if isinstance(node, node_3d):
                    node.physics_step(delta)
    
    def end(self):
        self.finished = True
            
class mesh(node_3d):
    def __init__(self, verts, indicies, texture):
        super().__init__()
        self.verts = verts
        self.indicies = indicies

        if max(self.indicies) >= len(self.verts):
            raise ValueError("one of your indicies is too big lil bro")

        if len(self.indicies) % 3 != 0:
            raise ValueError("indicies is not a multiple of 3")

        self.tris = [[self.verts[self.indicies[i*3]], self.verts[self.indicies[i*3+1]], self.verts[self.indicies[i*3+2]]] for i in range(len(self.indicies)//3)]

        self.texture = texture

class camera(node_3d):
    def __init__(self):
        super().__init__()

        self.screen_width = 150
        self.screen_height = 35
        self.horizontal_fov = 120
        self.vertical_fov = 90
        
        self.pitch = 0
        self.yaw = 0

        self.camera_translate_speed = 1
        self.camera_rotate_speed = 30
    
    def render(self, game_tree: game_tree):
        #doing something actually smart would be much faster but this is the first thing i thought of
        # a better solution might be finding each vertex and drawing the triangle between them

        α_start = self.pitch - self.vertical_fov/2
        θ_start = self.yaw   - self.horizontal_fov/2
        α_step  = self.vertical_fov   / self.screen_height
        θ_step  = self.horizontal_fov / self.screen_width

        lines = []
        for y in range(self.screen_height):
            α = α_start + y * α_step
            line = []
            for x in range(self.screen_width):
                θ = θ_start + x * θ_step
                line.append( self.cast_ray(game_tree, α, θ) )
            lines.append( ''.join(line) )

        print('\033[H\033[J', end='')
        print('\n'.join(lines))

    def cast_ray(self, game_tree, α, θ) -> str:
        dx =  math.cos(math.radians(α)) * math.cos(math.radians(θ))
        dy =  math.sin(math.radians(α))
        dz =  math.cos(math.radians(α)) * math.sin(math.radians(θ))
        dir = [dx, dy, dz]

        for node in game_tree.tree:
            if not isinstance(node, mesh):
                continue

            for tri in node.tris:
                if self.ray_intersects_triangle(self.pos, dir, tri):
                    return node.texture
        return " "

    def ray_intersects_triangle(self, origin, ray_dir, triangle):
        # THIS CODE WAS WRITTEN BY o4-mini btw (but just this part, the rest was me)

        # Möller–Trumbore ray intersection

        # origin, ray_dir: lists of 3 floats
        # triangle: list of three 3-floats lists [v0, v1, v2]
        EPSILON = 1e-8

        def vec_sub(a, b):
            return [a[0]-b[0], a[1]-b[1], a[2]-b[2]]

        def vec_dot(a, b):
            return a[0]*b[0] + a[1]*b[1] + a[2]*b[2]

        def vec_cross(a, b):
            return [
                a[1]*b[2] - a[2]*b[1],
                a[2]*b[0] - a[0]*b[2],
                a[0]*b[1] - a[1]*b[0]
            ]

        v0, v1, v2 = triangle
        edge1 = vec_sub(v1, v0)
        edge2 = vec_sub(v2, v0)

        # Begin calculating determinant - also used to calculate u parameter
        h = vec_cross(ray_dir, edge2)
        a = vec_dot(edge1, h)
        # If a is near zero, ray is parallel to triangle
        if -EPSILON < a < EPSILON:
            return False

        f = 1.0 / a
        # Calculate distance from v0 to ray origin
        s = vec_sub(origin, v0)
        u = f * vec_dot(s, h)
        if u < 0.0 or u > 1.0:
            return False

        # Prepare to test v parameter
        q = vec_cross(s, edge1)
        v = f * vec_dot(ray_dir, q)
        if v < 0.0 or u + v > 1.0:
            return False

        # At this stage we can compute t to find out where the intersection point is on the line
        t = f * vec_dot(edge2, q)
        # ray intersection
        if t > EPSILON:
            return True
        else:
            # This means there is a line intersection but not a ray intersection
            return False

    def camera_control(self, delta):
        '''
        if keyboard.is_pressed("esc"):
            game_tree.end()
        
        move_vect = [0,0,0]
        if keyboard.is_pressed("w"):
            move_vect[0] += 1
        if keyboard.is_pressed("s"):
            move_vect[0] -= 1
        if keyboard.is_pressed("d"):
            move_vect[2] += 1
        if keyboard.is_pressed("a"):
            move_vect[2] -= 1
        if keyboard.is_pressed("q"):
            move_vect[1] += 1
        if keyboard.is_pressed("e"):
            move_vect[1] -= 1

        rotate_vect = [0,0]
        if keyboard.is_pressed("up"):
            rotate_vect[0] -= 1
        if keyboard.is_pressed("down"):
            rotate_vect[0] += 1
        if keyboard.is_pressed("left"):
            rotate_vect[1] -= 1
        if keyboard.is_pressed("right"):
            rotate_vect[1] += 1
        
        if sum(move_vect) != 0:
            move_len_recip = 1/sum([abs(x) for x in move_vect])
            move_vect = [move_vect[i] * move_len_recip * delta * self.camera_translate_speed for i in range(3)]
            print(f"moving: {move_vect}")

        if sum(rotate_vect) != 0:
            rotate_len_recip = 1/sum([abs(x) for x in rotate_vect])
            rotate_vect = [rotate_vect[i] * rotate_len_recip * delta * self.camera_rotate_speed for i in range(2)]
            print(f"rotating: {rotate_vect}, {delta}")

        self.pos = [self.pos[i] + move_vect[i] for i in range(3)]
        self.pitch += rotate_vect[0]
        self.yaw += rotate_vect[1]
        '''

gt = game_tree()

cam = camera()
cam.pos = [-3, 0, 0]
gt.tree.append(cam)

gt.tree.append(mesh(
    [[0,0,0], [0,1,0], [0,1,1]],
    [0,1,2],
    "#"
))

verts = [
    [ 0,  1,  0],   # 0  apex
    [-1, -1,  1],   # 1  front-left
    [ 1, -1,  1],   # 2  front-right
    [ 1, -1, -1],   # 3  back-right
    [-1, -1, -1],   # 4  back-left
]

# 6 tris: 4 sides + 2 for the square base
indicies = [
    0,1,2,    # side 1
    0,2,3,    # side 2
    0,3,4,    # side 3
    0,4,1,    # side 4

    1,4,3,    # base (first triangle)
    1,3,2,    # base (second triangle)
]

pyramid = mesh(verts, indicies, "%")
gt.tree.append(pyramid)

gt.main()