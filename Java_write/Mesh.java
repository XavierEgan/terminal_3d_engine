package Java_write;

import java.util.ArrayList;
import java.util.Collections;
import Java_write.Util.*;

public class Mesh extends Node3D {
    public ArrayList<RenderingTriangle> triangles;

    public Mesh(ArrayList<Vec3> verts, ArrayList<Integer> indicies, char texture, String color) throws IllegalArgumentException {
        // every 3 indicies is a triangle, so it needs to be a multiple of 3
        if (indicies.size() % 3 != 0) {
            throw new IllegalArgumentException("indicies is not a multiple of 3");
        // the largest indicie should be less than the size of verts to avoid index out of range
        } else if (Collections.max(indicies) >= verts.size()) {
            throw new IllegalArgumentException("At least one of your indicies is too big");
        }

        this.triangles = new ArrayList<RenderingTriangle>();
        this.triangles.ensureCapacity(indicies.size() / 3); // more efficient because we dont have to constantly resize the internal array

        Triangle tri;
        RenderingTriangle rtri;

        // get all the triangles in a list
        // also dont copy the verts, so if i need to change the position of the verts i can without having to recalc the tris. For example if we need to rotate the mesh or translate it in space
        for (int i=0; i<indicies.size() / 3; i++) {
            tri = new Triangle();
            tri.vert0 = verts.get(indicies.get(i*3).intValue());
            tri.vert1 = verts.get(indicies.get(i*3 + 1).intValue());
            tri.vert2 = verts.get(indicies.get(i*3 + 2).intValue());

            rtri = new RenderingTriangle(tri, texture, color);
            
            this.triangles.add(rtri);
        }
        this.triangles.trimToSize(); // just incase i made the list too big before
    }

    public static Mesh triangle(Vec3 vert0, Vec3 vert1, Vec3 vert2, char texture, String color) {
        ArrayList<Vec3> verts = new ArrayList<Vec3>();
        verts.add(vert0);
        verts.add(vert1);
        verts.add(vert2);

        ArrayList<Integer> indicies = new ArrayList<Integer>();
        indicies.add(0); 
        indicies.add(1);
        indicies.add(2);
        

        return new Mesh(verts, indicies, texture, color);
    }

    public static Mesh squareBasedPyramid(double scale, Vec3 origin, char texture, String color) {
        // Create vertices list
        ArrayList<Vec3> verts = new ArrayList<Vec3>();
        // Apex vertex
        verts.add((new Vec3(0, 1, 0)).scaled(scale).add(origin)); // 0 - apex
        // Base vertices
        verts.add((new Vec3(-1, -1, 1)).scaled(scale).add(origin)); // 1 - front-left
        verts.add((new Vec3(1, -1, 1)).scaled(scale).add(origin)); // 2 - front-right
        verts.add((new Vec3(1, -1, -1)).scaled(scale).add(origin)); // 3 - back-right
        verts.add((new Vec3(-1, -1, -1)).scaled(scale).add(origin)); // 4 - back-left

        // Create indices list for triangles
        ArrayList<Integer> indices = new ArrayList<Integer>();
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
        return new Mesh(verts, indices, texture, color);
    }

    public static Mesh icoSphere(int subdivisions, Vec3 origin, char texture, String color) {
        // following this 
        // https://blog.lslabs.dev/posts/generating_icosphere_with_code

        // Finding the verticies
        Vec3[] icosphereVertices = {
            new Vec3(0.8506508f, 0.5257311f, 0f), // 0
            new Vec3(0.000000101405476f, 0.8506507f, -0.525731f), // 1
            new Vec3(0.000000101405476f, 0.8506506f, 0.525731f), // 2
            new Vec3(0.5257309f, -0.00000006267203f, -0.85065067f), // 3
            new Vec3(0.52573115f, -0.00000006267203f, 0.85065067f), // 4
            new Vec3(0.8506508f, -0.5257311f, 0f), // 5
            new Vec3(-0.52573115f, 0.00000006267203f, -0.85065067f), // 6
            new Vec3(-0.8506508f, 0.5257311f, 0f), // 7
            new Vec3(-0.5257309f, 0.00000006267203f, 0.85065067f), // 8
            new Vec3(-0.000000101405476f, -0.8506506f, -0.525731f), // 9
            new Vec3(-0.000000101405476f, -0.8506507f, 0.525731f), // 10
            new Vec3(-0.8506508f, -0.5257311f, 0f) // 11
		};
        ArrayList<Vec3> icoVerts = new ArrayList<Vec3>(icosphereVertices.length);
        for (Vec3 v : icosphereVertices) {
            icoVerts.add(v.add(origin));
        }

        int[] IcosphereIndices = {
            0, 1, 2,
            0, 3, 1,
            0, 2, 4,
            3, 0, 5,
            0, 4, 5,
            1, 3, 6,
            1, 7, 2,
            7, 1, 6,
            4, 2, 8,
            7, 8, 2,
            9, 3, 5,
            6, 3, 9,
            5, 4, 10,
            4, 8, 10,
            9, 5, 10,
            7, 6, 11,
            7, 11, 8,
            11, 6, 9,
            8, 11, 10,
            10, 11, 9
		};
        ArrayList<Integer> icoInts = new ArrayList<Integer>(icosphereVertices.length);
        for (Integer i : IcosphereIndices) {
            icoInts.add(i);
        }

        return new Mesh(icoVerts, icoInts, texture, color);
    }
}