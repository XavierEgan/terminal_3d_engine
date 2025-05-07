package Java_write.Render_Methods;

import Java_write.Util.*;
import Java_write.*;

import java.util.ArrayList;

public class NaiveRewrite {
    public static void render(SceneTree sceneTree, Camera cam) {
        // first get all the tris in the scene into a tri buffer 
        ArrayList<RenderingTriangle> tris = getTris(sceneTree);

        double yaw = cam.rot.y - cam.horizontalFov/2;
        double yawStep = cam.horizontalFov / cam.screenWidth;

        double pitch = cam.rot.z + cam.verticalFov / 2;
        double pitchStep = cam.verticalFov / cam.screenHeight;

        StringBuilder screen = new StringBuilder((cam.screenHeight * cam.screenWidth) * 20 + cam.screenHeight); // *20 to account for ansi escape codes
        screen.append("\033[H");

        for (int pitchI=0; pitchI < cam.screenHeight; pitchI++) {
            for (int yawI=0; yawI < cam.horizontalFov; yawI++) {
                // get the dir vector of the ray were casting
                Vec3 dir = Vec3.fromSpherical(pitch, yaw);

                double intersectionDistance;
                double minIntersectionDistance = Double.POSITIVE_INFINITY;
                RenderingTriangle closestIntersectingTriangle = null;

                // go through every triangle
                for (RenderingTriangle rtri : tris) {
                    // check if the ray is intersecting the tri
                    intersectionDistance = rtri.tri.rayIntersectionDistance(cam.pos, dir);

                    if (intersectionDistance < 0) {
                        // we are not intersecting
                        continue;
                    }
                    
                    if (intersectionDistance < minIntersectionDistance) {
                        // the intersection is closer
                        minIntersectionDistance = intersectionDistance;
                        closestIntersectingTriangle = rtri;
                    }
                }
                if (closestIntersectingTriangle == null) {
                    // there was no triangle intersection
                    screen.append(" ");
                } else {
                    String pixel = closestIntersectingTriangle.color + closestIntersectingTriangle.texture + "\033[0m";
                    screen.append(pixel);
                }

                // sweep across the screen for each line
                yaw += yawStep;
            }
            // add the newline
            screen.append("\n");

            // go back to the start for the next line
            yaw = cam.rot.y - cam.horizontalFov / 2;

            // look down a lil bit
            pitch -= pitchStep;
        }
        System.out.println(screen);
    }
    
    public static ArrayList<RenderingTriangle> getTris(SceneTree sceneTree) {
        // get all the meshs
        ArrayList<Mesh> meshs = new ArrayList<Mesh>();

        for (Node n : sceneTree.tree) {
            if (n instanceof Mesh) {
                meshs.add((Mesh) n);
            }
        }
        
        // get all the tris
        ArrayList<RenderingTriangle> tris = new ArrayList<RenderingTriangle>();
        for (Mesh m : meshs) {
            for (RenderingTriangle t : m.triangles) {
                tris.add(t);
            }
        }

        return tris;
    }
}