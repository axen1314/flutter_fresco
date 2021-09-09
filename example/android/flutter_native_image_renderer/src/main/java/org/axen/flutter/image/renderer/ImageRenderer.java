package org.axen.flutter.image.renderer;

import org.axen.flutter.image.renderer.entity.NativeImage;

import io.flutter.plugin.common.MethodChannel;

public interface ImageRenderer<T> {
    void render(NativeImage info, MethodChannel.Result result);
    void release();
}
