package Java_write.Util;

// yes Vec3 being immutable is slower, no i dont care
public class Vec3 {
    public final double x, y, z;

    public Vec3() {
        this(0.0, 0.0, 0.0);
    }

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec3 fromSpherical(double pitch, double yaw) {
        pitch = Math.toRadians(pitch);
        yaw = Math.toRadians(yaw);
        // angles in degrees
        return new Vec3(
            Math.cos(yaw) * Math.cos(pitch),
            Math.sin(pitch),
            Math.sin(yaw) * Math.cos(pitch)
        );
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(
            this.x + other.x,
            this.y + other.y,
            this.z + other.z
        );
    }

    public Vec3 sub(Vec3 other) {
        return new Vec3(
            this.x - other.x,
            this.y - other.y,
            this.z - other.z
        );
    }

    public Vec3 scaled(double scale) {
        return new Vec3(
            this.x * scale,
            this.y * scale,
            this.z * scale
        );
    }

    public Vec3 normalized() {
        double len = length();
        if (len != 0 || len == 1.0)
            return this.scaled(1.0 / len);
        else {
            return this;
        }
    }

    public double length() {
        // pythag - so just dot itself with itself (sum of squares of terms) and then sqrt it which is just pythags theorem
        return Math.sqrt(dot(this));
    }

    public double dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vec3 cross(Vec3 other) {
        //s1 = a2*b3 - a3*b2
        double s1 = this.y*other.z - this.z*other.y;

        //s2 = a3*b1 - a1*b3
        double s2 = this.z*other.x - this.x*other.z;

        //s3 = a1*b2 - a2*b1
        double s3 = this.x*other.y - this.y*other.x;

        return (new Vec3(s1, s2, s3));
    }

    public Vec3 project(Vec3 other) {
        // (a.bhat) * bhat
        // projects in the direction of other
        Vec3 otherhat = other.normalized();
        return otherhat.scaled(this.dot(otherhat));
        
    }

    @Override
    public String toString() {
        return String.format("Vec3(%.3f, %.3f, %.3f)", this.x, this.y, this.z);
    }
}