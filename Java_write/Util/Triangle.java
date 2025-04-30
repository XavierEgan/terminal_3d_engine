package Java_write.Util;

public class Triangle {
    public Vec3 vert0;
    public Vec3 vert1;
    public Vec3 vert2;

    public Triangle(Vec3 vert0, Vec3 vert1, Vec3 vert2) {
        this.vert0 = vert0;
        this.vert1 = vert1;
        this.vert2 = vert2;
    }

    public Triangle() {
        this(new Vec3(), new Vec3(), new Vec3());
    }

    public Vec3 normal() {
        // normal is the cross product of two of the edges
        // ABxBC
        // (OB - OA)x(OC - OB)
        return vert1.sub(vert0).cross(vert2.sub(vert0));
    }

    public void translate(Vec3 offset) {
        this.vert0 = vert0.add(offset);
        this.vert1 = vert1.add(offset);
        this.vert2 = vert2.add(offset);
    }
    // I copy pasted the same code 3 times because it *might* be faster for performance however i havent benchmarked it
    // plus its easier to write so sorry to me in the future if i ever need to change this
    public Vec3 rayIntersectionPoint(Vec3 origin, Vec3 dir) {
        // impliments the Möller–Trumbore intersection algorithm
        // algorithm is copied (and modified)from here: https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm
        Vec3 edge1;
        Vec3 edge2;
        Vec3 h;
        Vec3 s;
        Vec3 q;
        double a, f, u, v;
        double EPSILON = 1e-6;

        //normalize the dir vector
        dir = dir.normalized();

        edge1 = vert1.sub(vert0);
        edge2 = vert2.sub(vert0);
        h = dir.cross(edge2);
        a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return null; // This ray is parallel to this triangle.
        }

        f = 1.0 / a;
        s = origin.sub(vert0);
        u = f * (s.dot(h));

        if (u < 0.0 || u > 1.0) {
            return null;
        }

        q = s.cross(edge1);
        v = f * dir.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return null;
        }

        // At this stage we can compute t to find out where the intersection point is on
        // the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {   
            Vec3 intPont = origin.add(dir.scaled(t));
            return intPont;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return null;
        }
    }

    public double rayIntersectionDistance(Vec3 origin, Vec3 dir) {
        // impliments the Möller–Trumbore intersection algorithm
        // algorithm is copied (and modified)from here:
        // https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm
        Vec3 edge1;
        Vec3 edge2;
        Vec3 h;
        Vec3 s;
        Vec3 q;
        double a, f, u, v;
        double EPSILON = 1e-6;

        // normalize the dir vector
        dir = dir.normalized();

        edge1 = vert1.sub(vert0);
        edge2 = vert2.sub(vert0);
        h = dir.cross(edge2);
        a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return -1.0; // This ray is parallel to this triangle.
        }

        f = 1.0 / a;
        s = origin.sub(vert0);
        u = f * (s.dot(h));

        if (u < 0.0 || u > 1.0) {
            return -1.0;
        }

        q = s.cross(edge1);
        v = f * dir.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return -1.0;
        }

        // At this stage we can compute t to find out where the intersection point is on
        // the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            // my vec3 implimentation is immutable
            // outIntersectionPoint.set(0.0, 0.0, 0.0);
            // outIntersectionPoint.scaleAdd(t, rayVector, rayOrigin);
            return t;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return -1.0;
        }
    }

    public boolean rayIntersects(Vec3 origin, Vec3 dir) {
        // impliments the Möller–Trumbore intersection algorithm
        // algorithm is copied (and modified)from here:
        // https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm
        Vec3 edge1;
        Vec3 edge2;
        Vec3 h;
        Vec3 s;
        Vec3 q;
        double a, f, u, v;
        double EPSILON = 1e-6;

        // normalize the dir vector
        dir = dir.normalized();

        edge1 = vert1.sub(vert0);
        edge2 = vert2.sub(vert0);
        h = dir.cross(edge2);
        a = edge1.dot(h);

        if (a > -EPSILON && a < EPSILON) {
            return false; // This ray is parallel to this triangle.
        }

        f = 1.0 / a;
        s = origin.sub(vert0);
        u = f * (s.dot(h));

        if (u < 0.0 || u > 1.0) {
            return false;
        }

        q = s.cross(edge1);
        v = f * dir.dot(q);

        if (v < 0.0 || u + v > 1.0) {
            return false;
        }

        // At this stage we can compute t to find out where the intersection point is on
        // the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            return true;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return false;
        }
    }
}
