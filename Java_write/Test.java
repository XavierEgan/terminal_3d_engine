package Java_write;

import java.util.ArrayList;
import Java_write.Util.*;

public class Test {
    public static void main(String[] args) {
        scene1();
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

        Benchmark bench = new Benchmark(1000);
        sceneTree.register(bench);

        sceneTree.mainLoop();
    }

    public static void scene2() {
        
    }
}
