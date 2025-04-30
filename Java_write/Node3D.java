package Java_write;

import Java_write.Util.*;

public class Node3D extends Node{
    // x = forward/back || +ve x yaw = 0, forward
    // y = up/down
    // z = right/left || +ve z yaw = 90, right
    public Vec3 pos;
    public Vec3 rot;
    public Node3D(Vec3 pos, Vec3 rot) {
        this.pos = pos;
        this.rot = rot;
    }

    public Node3D() {
        this.pos = new Vec3();
    }

    @Override
    public void tick(double delta, SceneTree sceneTree) {
        physics();
    }

    private void physics() {
        
    }
}
