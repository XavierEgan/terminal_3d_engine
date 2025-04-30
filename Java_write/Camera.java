package Java_write;

import Java_write.Util.*;
import java.util.ArrayList;

public class Camera extends Node3D{
    private double horizontalFov, verticalFov;
    private int screenHeight, screenWidth;

    public Camera(Vec3 pos, Vec3 rot, double horizontalFov, double verticalFov, int screenHeight, int screenWidth) {
        super(pos, rot);
        this.horizontalFov = horizontalFov;
        this.verticalFov = verticalFov;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public Camera() {
        this(new Vec3(), new Vec3(), 120.0, 90.0, 35, 150);
    }

    @Override
    public void tick(double delta, SceneTree sceneTree) {
        render(sceneTree, delta);
    }

    private void render(SceneTree sceneTree, double delta) {
        naiveMethod(sceneTree, delta);
    }

    private void naiveMethod(SceneTree sceneTree, double delta) {
        // this checks a ray casted for each pixel against every single triangle. This is the bottom of the barrel, only up from here
        // prolly got a O(N!!!!!!!!!) or some shi

        // get all the meshes in the scene
        ArrayList<Mesh> meshs= new ArrayList<Mesh>();
        for (Node n : sceneTree.tree) {
            if (n instanceof Mesh) {
                meshs.add((Mesh) n);
            }
        }

        double rowAngle = rot.z - verticalFov/2;
        double rowStep = verticalFov/this.screenHeight;

        double colAngle = rot.y - horizontalFov/2;
        double colStep = horizontalFov/this.screenWidth;

        String pixelTexture = null;
        double closestPixelDepth = 1e9;
        double dist;

        int estimatedChars = this.screenHeight * this.screenWidth + this.screenHeight;
        StringBuilder screen = new StringBuilder(estimatedChars);

        Vec3 dir;
        // for each row on the screen
        for (int row=0; row < this.screenHeight; row++) {
            // for each col on the row
            for (int col=0; col < this.screenWidth; col++) {
                dir = Vec3.fromSpherical(rowAngle, colAngle);
                closestPixelDepth = 1e9;
                pixelTexture = null;
                // for each mesh in the scene
                for (Mesh m : meshs) {
                    // for each tri in the mesh
                    for (Triangle t : m.triangles) {
                        dist = t.rayIntersectionDistance(this.pos, dir);
                        if (dist < 0) {
                            continue;
                        }
                        if (dist < closestPixelDepth) {
                            pixelTexture = m.texture;
                            closestPixelDepth = dist;
                        }
                    }
                }
                if (pixelTexture != null) {
                    screen.append(pixelTexture);
                } else {
                    screen.append(" ");
                }
                colAngle += colStep;
            }
            screen.append("\n");
            rowAngle += rowStep;
            colAngle = rot.y - horizontalFov/2;
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(screen);
        System.out.printf("Java FPS: %.2f", 1.0/delta);
    }
}
