package Java_write;

import Java_write.Util.*;
import java.util.ArrayList;

public class CameraTrack extends Node3D {

    private ArrayList<Vec3> cameraTrack; // since the time is fixed (fps) we dont need to store the corresponding time.
    private ArrayList<Vec3> cameraRotateTrack;

    private Camera camera;
    private int fps;

    public CameraTrack(Camera camera, int fps) {
        this.camera = camera;
        this.fps = fps;
        cameraTrack = new ArrayList<Vec3>();
        cameraRotateTrack = new ArrayList<Vec3>();
    }

    @Override
    public void tick(double delta, SceneTree sceneTree) {
        // frames / sec * sec = frames
        
        int elapsedFrames = (int) (fps * (sceneTree.elapsedTime));

        if (elapsedFrames < cameraTrack.size()) {
            camera.pos = cameraTrack.get(elapsedFrames);
        }

        if (elapsedFrames < cameraRotateTrack.size()) {
            camera.rot = cameraRotateTrack.get(elapsedFrames);
        }
    }

    public void move_to(Vec3 to, double time, EasingType easingType) {
        int frames = (int) (fps * time);

        cameraTrack.ensureCapacity(frames + cameraTrack.size());
        double t;

        Vec3 from;

        if (this.cameraTrack.size() == 0) {
            from = camera.pos;
        } else {
            from = cameraTrack.getLast();
        }

        for (int frame=0; frame < frames; frame++) {
            t = (double)frame/frames;
            cameraTrack.add(arbitraryLerp(from, to, t, easingType));
        }

        cameraTrack.trimToSize();
    }

    public void rotate_to(Vec3 to, double time, EasingType easingType) {
        int frames = (int) (fps * time);

        cameraRotateTrack.ensureCapacity(frames + cameraRotateTrack.size());
        double t;

        Vec3 from;

        if (this.cameraRotateTrack.size() == 0) {
            from = camera.rot;
        } else {
            from = cameraRotateTrack.getLast();
        }

        for (int frame = 0; frame < frames; frame++) {
            t = (double) frame / frames;
            cameraRotateTrack.add(arbitraryLerp(from, to, t, easingType));
        }

        cameraRotateTrack.trimToSize();
    }

    private Vec3 arbitraryLerp(Vec3 start, Vec3 end, double t, EasingType easingType) {
        switch (easingType) {
            case EasingType.LINEAR:
                return lerp(start, end, t);
            case EasingType.QUADRATIC:
                return lerp(start, end, Math.pow(t, 2));
            case EasingType.CUBIC:
                return lerp(start, end, Math.pow(t, 3));
            case EasingType.SIN:
                return lerp(start, end, 0.5-0.5*Math.cos(t*Math.PI));
            default:
                return lerp(start, end, t);
        }
    }

    private Vec3 lerp(Vec3 start, Vec3 end, double t) {
        // a + (b - a) * f(t)
        return start.add(end.sub(start).scaled(t));
    }
}
