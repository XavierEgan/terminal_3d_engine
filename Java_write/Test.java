package Java_write;

import java.util.ArrayList;
import Java_write.Util.*;;

public class Test {
    public static void main(String[] args) {
        scene1();
    }

    public static void scene1() {
        SceneTree sceneTree = new SceneTree();

        ArrayList<Vec3> verts = new ArrayList<Vec3>();
        ArrayList<Integer> indices = new ArrayList<Integer>();

        verts.add(new Vec3(1,0,0));
        verts.add(new Vec3(1, 1, 0));
        verts.add(new Vec3(1, 1, 1));

        indices.add(0); 
        indices.add(1);
        indices.add(2);

        Mesh triangle = new Mesh(verts, indices, "#");
        sceneTree.register(triangle);

        // Create vertices list
        verts = new ArrayList<Vec3>();
        // Apex vertex
        verts.add(new Vec3(0, 1, 0)); // 0 - apex
        // Base vertices
        verts.add(new Vec3(-1, -1, 1)); // 1 - front-left
        verts.add(new Vec3(1, -1, 1)); // 2 - front-right
        verts.add(new Vec3(1, -1, -1)); // 3 - back-right
        verts.add(new Vec3(-1, -1, -1)); // 4 - back-left

        // Create indices list for triangles
        indices = new ArrayList<Integer>();
        // Four sides
        indices.add(0);
        indices.add(1);
        indices.add(2); // front face
        indices.add(0);
        indices.add(2);
        indices.add(3); // right face
        indices.add(0);
        indices.add(3);
        indices.add(4); // back face
        indices.add(0);
        indices.add(4);
        indices.add(1); // left face

        // Base (split into two triangles)
        indices.add(1);
        indices.add(4);
        indices.add(3); // base triangle 1
        indices.add(1);
        indices.add(3);
        indices.add(2); // base triangle 2

        // Create mesh and add to scene
        Mesh pyramid = new Mesh(verts, indices, "%"); // Or whatever texture you want
        sceneTree.register(pyramid);

        Camera camera = new Camera(new Vec3(-5,0,0), new Vec3(0, 0, 0), 120, 90, 35, 150);
        sceneTree.register(camera);

        sceneTree.mainLoop();
    }
}
