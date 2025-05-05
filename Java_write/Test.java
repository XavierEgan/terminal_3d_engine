package Java_write;

import Java_write.Util.*;

public class Test {
    public static void main(String[] args) {
        scene2();
    }

    public static void scene1() {
        SceneTree sceneTree = new SceneTree();

        Mesh triangle = Mesh.triangle(new Vec3(0,0,0), new Vec3(0,1,0), new Vec3(0,1,1), ' ', "\033[41m");
        sceneTree.register(triangle);

        // Create mesh and add to scene
        Mesh pyramid = Mesh.squareBasedPyramid(1, new Vec3(), ' ', "\033[42m");
        sceneTree.register(pyramid);

        Camera camera = new Camera(new Vec3(-5,0,0), new Vec3(0, 0, 0), 120, 90, 35, 150);
        sceneTree.register(camera);

        CameraTrack cameraTrack = new CameraTrack(camera, 60);
        cameraTrack.move_to(new Vec3(-5,0,2), 2, EasingType.SIN);
        cameraTrack.move_to(new Vec3(-5, 0, -2), 4, EasingType.LINEAR);
        sceneTree.register(cameraTrack);

        Benchmark bench = new Benchmark(1000);
        sceneTree.register(bench);

        sceneTree.mainLoop();
    }

    public static void scene2() {
        SceneTree sceneTree = new SceneTree();

        // Ground (2 triangles)
        Mesh ground1 = Mesh.triangle(
                new Vec3(-5, -1, -5),
                new Vec3(5, -1, -5),
                new Vec3(5, -1, 5),
                ' ', "\033[42m");
        sceneTree.register(ground1);
        Mesh ground2 = Mesh.triangle(
                new Vec3(-5, -1, -5),
                new Vec3(5, -1, 5),
                new Vec3(-5, -1, 5),
                ' ', "\033[42m");
        sceneTree.register(ground2);

        // House (square‐based pyramid = 6 triangles)
        Mesh house = Mesh.squareBasedPyramid(
                2,
                new Vec3(0, -1, 0),
                ' ', "\033[41m");
        sceneTree.register(house);

        // Sun (1 triangle)
        Mesh sun = Mesh.triangle(
                new Vec3(-4, 3, -3),
                new Vec3(-2, 3, -3),
                new Vec3(-3, 4, -3),
                ' ', "\033[43m");
        sceneTree.register(sun);

        // Tree trunk (2 triangles)
        Mesh trunk1 = Mesh.triangle(
                new Vec3(2, -1, 2),
                new Vec3(2, 1, 2),
                new Vec3(1, -1, 2),
                ' ', "\033[45m");
        sceneTree.register(trunk1);
        Mesh trunk2 = Mesh.triangle(
                new Vec3(1, -1, 2),
                new Vec3(2, 1, 2),
                new Vec3(1, 1, 2),
                ' ', "\033[45m");
        sceneTree.register(trunk2);

        // Leaves (1 triangle)
        Mesh leaves = Mesh.triangle(
                new Vec3(1, 1, 2),
                new Vec3(3, 1, 2),
                new Vec3(2, 2, 2),
                ' ', "\033[42m");
        sceneTree.register(leaves);

        // Rock (1 triangle)
        Mesh rock = Mesh.triangle(
                new Vec3(-2, -1, 3),
                new Vec3(-1, -1, 3),
                new Vec3(-1, -0.5, 2),
                ' ', "\033[46m");
        sceneTree.register(rock);

        // Camera
        Camera camera = new Camera(
                new Vec3(-10, 5, 10), // position
                new Vec3(0, 0, 0), // look‐at
                120, 90, 35, 150);
        sceneTree.register(camera);

        // Camera track: circle around the scene
        CameraTrack track = new CameraTrack(camera, 12);
        track.move_to(new Vec3(10, 5, 10), 4, EasingType.SIN);
        track.move_to(new Vec3(10, 5, -10), 4, EasingType.LINEAR);
        track.move_to(new Vec3(-10, 5, -10), 4, EasingType.SIN);
        track.move_to(new Vec3(-10, 5, 10), 4, EasingType.LINEAR);
        sceneTree.register(track);

        // Benchmark and start
        Benchmark bench = new Benchmark(1000);
        sceneTree.register(bench);

        sceneTree.mainLoop();

    }

    
}
