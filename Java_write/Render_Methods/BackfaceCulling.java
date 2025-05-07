package Java_write.Render_Methods;

import Java_write.Util.*;
import Java_write.*;

import java.util.ArrayList;

public class BackfaceCulling {
    public static void render(SceneTree sceneTree, Camera cam) {
        // first get all the tris in the scene into a tri buffer 
        ArrayList<RenderingTriangle> tris = getTris(sceneTree, Vec3.fromSpherical(cam.rot.z, cam.rot.y));

        StringBuilder screen = new StringBuilder((cam.screenHeight * cam.screenWidth) * 20 + cam.screenHeight); // *20 to account for ansi escape codes
        screen.append("\033[H");

        for (int pitchI=0; pitchI < cam.screenHeight; pitchI++) {
            for (int yawI=0; yawI < cam.horizontalFov; yawI++) {
                // get the dir vector of the ray were casting
                Vec3 dir = cam.rays[pitchI][yawI];

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
            }
            // add the newline
            screen.append("\n");
        }
        System.out.println(screen);
    }
    
    public static ArrayList<RenderingTriangle> getTris(SceneTree sceneTree, Vec3 camFacingVec3) {
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
            for (RenderingTriangle rtri : m.triangles) {
                if (drawTri(rtri.tri, camFacingVec3)) {
                    tris.add(rtri);
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