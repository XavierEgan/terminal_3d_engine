package Java_write;

import java.util.ArrayList;
import java.util.Collections;
import Java_write.Util.*;

public class Mesh extends Node3D {
    public ArrayList<Triangle> triangles;

    public Mesh(ArrayList<Vec3> verts, ArrayList<Integer> indicies, char texture, String color) throws IllegalArgumentException {
        // every 3 indicies is a triangle, so it needs to be a multiple of 3
        if (indicies.size() % 3 != 0) {
            throw new IllegalArgumentException("indicies is not a multiple of 3");
        // the largest indicie should be less than the size of verts to avoid index out of range
        } else if (Collections.max(indicies) >= verts.size()) {
            throw new IllegalArgumentException("At least one of your indicies is too big");
        }

        this.triangles = new ArrayList<Triangle>();
        this.triangles.ensureCapacity(indicies.size() / 3); // more efficient because we dont have to constantly resize the internal array

        Triangle tri;

        // get all the triangles in a list
        // also dont copy the verts, so if i need to change the position of the verts i can without having to recalc the tris. For example if we need to rotate the mesh or translate it in space
        for (int i=0; i<indicies.size() / 3; i++) {
            tri = new Triangle();
            tri.vert0 = verts.get(indicies.get(i*3).intValue());
            tri.vert1 = verts.get(indicies.get(i*3 + 1).intValue());
            tri.vert2 = verts.get(indicies.get(i*3 + 2).intValue());

            tri.color = color;
            tri.texture = texture;
            
            this.triangles.add(tri);
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
}