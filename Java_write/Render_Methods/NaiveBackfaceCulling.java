package Java_write.Render_Methods;

import Java_write.Util.*;
import Java_write.*;

import java.util.ArrayList;

public class NaiveBackfaceCulling {
    public static void render(SceneTree sceneTree, Camera cam) {
        // first get all the tris in the scene into a tri buffer 
        ArrayList<Triangle> tris = getTris(sceneTree, Vec3.fromSpherical(cam.rot.z, cam.rot.y));

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
                Triangle closestIntersectingTriangle = null;

                // go through every triangle
                for (Triangle tri : tris) {
                    // check if the ray is intersecting the tri
                    intersectionDistance = tri.rayIntersectionDistance(cam.pos, dir);

                    if (intersectionDistance < 0) {
                        // we are not intersecting
                        continue;
                    }
                    
                    if (intersectionDistance < minIntersectionDistance) {
                        // the intersection is closer
                        minIntersectionDistance = intersectionDistance;
                        closestIntersectingTriangle = tri;
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
    
    public static ArrayList<Triangle> getTris(SceneTree sceneTree, Vec3 camFacingVec3) {
        // get all the meshs
        ArrayList<Mesh> meshs = new ArrayList<Mesh>();

        for (Node n : sceneTree.tree) {
            if (n instanceof Mesh) {
                meshs.add((Mesh) n);
            }
        }
        
        // get all the tris
        ArrayList<Triangle> tris = new ArrayList<Triangle>();
        int i = 0;
        for (Mesh m : meshs) {
            for (Triangle t : m.triangles) {
                if (drawTri(t, camFacingVec3)) {
                    tris.add(t);
                    i++;
                }
            }
        }

        return tris;
    }

    public static boolean drawTri(Triangle tri, Vec3 camFacingVec3) {
        Vec3 normal = tri.normal();

        // project the normal vector onto the forward vector
        Vec3 projection = normal.project(camFacingVec3);

        // check if its positive or negative
        // to do this, get the first value and calculate x in forward * x = proj
        if (projection.x / camFacingVec3.z > 0) {
            return true;
        } else {
            return false;
        }
    }
}