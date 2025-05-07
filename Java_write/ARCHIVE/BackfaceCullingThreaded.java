package Java_write.ARCHIVE;

import java.util.ArrayList;

import Java_write.*;
import Java_write.Util.*;

public class BackfaceCullingThreaded {
    public static void render(SceneTree sceneTree, Camera cam) {
        ArrayList<Triangle> tris = getTris(sceneTree, Vec3.fromSpherical(cam.rot.z, cam.rot.y));

        // each line is a single job
        final int NUM_THREADS = 16;

        // we want each thread to have either jobs_per_thread or jobs_per_thread+1 jobs
        // jobs_leftover threads will have extra jobs
        BackfaceCullingThreadedWorker[] workers =  new BackfaceCullingThreadedWorker[NUM_THREADS];

        int jobs_per_thread = cam.screenHeight/NUM_THREADS; // floor
        int jobs_leftover = cam.screenHeight % NUM_THREADS;
        int num_jobs;
        int job_index = 0;

        Vec3[][] job;
        // loop through each thread
        for (int i=0; i < NUM_THREADS; i++) {
            // give the thread either jobs_per_thread or jobs_per_thread+1 jobs
            if (i < jobs_leftover) {
                // we need to add an extra job to this thread
                num_jobs = jobs_per_thread+1;
            } else {
                // we dont need to add an extra job
                num_jobs = jobs_per_thread;
            }
            job = new Vec3[num_jobs][cam.screenHeight];
            for (int j=0; j<num_jobs; j++) {
                job[j] = cam.rays[job_index];

                job_index++;
            }
            workers[i] = new BackfaceCullingThreadedWorker(tris, cam, job);
        }

        Thread[] threads = new Thread[NUM_THREADS];
        for (int i=0; i<NUM_THREADS; i++) {
            threads[i] = new Thread(workers[i]);
            threads[i].start();
        }

        // wait for all the threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        // construct the screen
        StringBuilder screen = new StringBuilder();
        screen.append("\033[H");
        for (BackfaceCullingThreadedWorker worker : workers) {
            screen.append(worker.screen);
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
        for (Mesh m : meshs) {
            for (Triangle t : m.triangles) {
                if (drawTri(t, camFacingVec3)) {
                    tris.add(t);
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