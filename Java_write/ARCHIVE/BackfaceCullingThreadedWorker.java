package Java_write.ARCHIVE;

import java.util.ArrayList;

import javax.script.ScriptEngineManager;

import Java_write.*;
import Java_write.Util.*;

public class BackfaceCullingThreadedWorker implements Runnable {
    ArrayList<Triangle> tris;
    Vec3[][] rays;
    Camera cam;

    StringBuilder screen;

    public BackfaceCullingThreadedWorker(ArrayList<Triangle> tris, Camera cam, Vec3[][] rays) {
        this.tris = tris;
        this.cam = cam;
        this.rays = rays;

        screen = new StringBuilder();
    }

    @Override
    public void run() {
        for (int pitchI = 0; pitchI < rays.length; pitchI++) {
            for (int yawI = 0; yawI < cam.screenWidth; yawI++) {
                // get the dir vector of the ray were casting
                Vec3 dir = rays[pitchI][yawI];

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
            }
            // add the newline
            screen.append("\n");
        }
    }
}