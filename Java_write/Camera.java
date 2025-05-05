package Java_write;

import Java_write.Util.*;
import Java_write.Render_Methods.*;

public class Camera extends Node3D{
    public double horizontalFov, verticalFov;
    public int screenHeight, screenWidth;

    public Camera(Vec3 pos, Vec3 rot, double horizontalFov, double verticalFov, int screenHeight, int screenWidth) {
        super(pos, rot);
        this.horizontalFov = horizontalFov;
        this.verticalFov = verticalFov;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public Camera() {
        this(new Vec3(), new Vec3(), 120.0, 90.0, 35, 150);
    }

    @Override
    public void tick(double delta, SceneTree sceneTree) {
        NaiveBackfaceCulling.render(sceneTree, this);
    }
}
