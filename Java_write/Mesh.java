package Java_write;

import java.util.ArrayList;
import java.util.Collections;
import Java_write.Util.*;

class Mesh extends Node3D {
    public ArrayList<Triangle> triangles;
    public String texture;

    public Mesh(ArrayList<Vec3> verts, ArrayList<Integer> indicies, String texture) throws IllegalArgumentException {
        // every 3 indicies is a triangle, so it needs to be a multiple of 3
        if (indicies.size() % 3 != 0) {
            throw new IllegalArgumentException("indicies is not a multiple of 3");
        // the largest indicie should be less than the size of verts to avoid index out of range
        } else if (Collections.max(indicies) >= verts.size()) {
            throw new IllegalArgumentException("At least one of your indicies is too big");
        }

        this.texture = texture;

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
            
            this.triangles.add(tri);
        }
        this.triangles.trimToSize(); // just incase i made the list too big before
    }
}