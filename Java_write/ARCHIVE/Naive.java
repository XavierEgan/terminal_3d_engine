package Java_write.Render_Methods;

import java.util.ArrayList;

import Java_write.*;
import Java_write.Util.*;

public class Naive {
    private static ArrayList<Mesh> getMeshs(SceneTree sceneTree) {
        ArrayList<Mesh> meshs = new ArrayList<Mesh>();
        for (Node n : sceneTree.tree) {
            if (n instanceof Mesh) {
                meshs.add((Mesh) n);
            }
        }

        return meshs;
    }

    public static void render(SceneTree sceneTree, Camera cam) {
        // cam checks a ray casted for each pixel against every single triangle. cam is the bottom of the barrel, only up from here
        // prolly got a O(N!!!!!!!!!) or some shi

        // get all the meshes in the scene
        ArrayList<Mesh> meshs = getMeshs(sceneTree);

        double rowAngle = cam.rot.z - cam.verticalFov/2;
        double rowStep = cam.verticalFov/ cam.screenHeight;

        double colAngle = cam.rot.y - cam.horizontalFov/2;
        double colStep = cam.horizontalFov/cam.screenWidth;

        String pixelTexture = null;
        double closestPixelDepth = 1e9;
        double dist;

        int estimatedChars = cam.screenHeight * cam.screenWidth + cam.screenHeight;
        StringBuilder screen = new StringBuilder(estimatedChars);

        Vec3 dir;
        // for each row on the screen
        for (int row=0; row < cam.screenHeight; row++) {
            // for each col on the row
            for (int col=0; col < cam.screenWidth; col++) {
                dir = Vec3.fromSpherical(rowAngle, colAngle);
                closestPixelDepth = 1e9;
                pixelTexture = null;
                // for each mesh in the scene
                for (Mesh m : meshs) {
                    // for each tri in the mesh
                    for (Triangle t : m.triangles) {
                        dist = t.rayIntersectionDistance(cam.pos, dir);
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
            colAngle = cam.rot.y - cam.horizontalFov/2;
        }
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println(screen);
    }
}
