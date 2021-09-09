package org.axen.flutter.image.renderer.entity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class NativeImagePool {
    private static final ConcurrentLinkedQueue<NativeImage> POOL = new ConcurrentLinkedQueue<>();

    public static NativeImage obtain() {
        NativeImage info = POOL.poll();
        return info != null ? info : new NativeImage();
    }

    public static void recycle(NativeImage info) {
        POOL.add(info.recycle());
    }
}
