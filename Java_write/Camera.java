package Java_write;

import Java_write.Util.*;
import Java_write.Render_Methods.*;

public class Camera extends Node3D{
    public double horizontalFov, verticalFov;
    public int screenHeight, screenWidth;
    public Vec3[][] rays;

    public Camera(Vec3 pos, Vec3 rot, double horizontalFov, double verticalFov, int screenHeight, int screenWidth) {
        super(pos, rot);
        this.horizontalFov = horizontalFov;
        this.verticalFov = verticalFov;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        // precompute the rays
        rays = new Vec3[screenHeight][screenWidth];

        double yaw_start = rot.y - horizontalFov/2;
        double yaw = yaw_start;
        double yawStep = horizontalFov / screenWidth;

        double pitch_start = rot.z + verticalFov / 2;
        double pitch = pitch_start;
        double pitchStep = verticalFov / screenHeight;

        for (int pitchI=0; pitchI < screenHeight; pitchI++) {
            for (int yawI=0; yawI < horizontalFov; yawI++) {
                // get the dir vector of the ray were casting
                rays[pitchI][yawI] = Vec3.fromSpherical(pitch, yaw);

                // sweep across the screen for each line
                yaw += yawStep;
            }
            // go back to the start for the next line
            yaw = yaw_start;

            // look down a lil bit
            pitch -= pitchStep;
        }
    }

    public Camera() {
        this(new Vec3(), new Vec3(), 120.0, 90.0, 35, 150);
    }

    @Override
    public void tick(double delta, SceneTree sceneTree) {
        NaiveBackfaceCulling.render(sceneTree, this);
    }
}
