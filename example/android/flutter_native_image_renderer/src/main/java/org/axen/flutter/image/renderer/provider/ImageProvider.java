package org.axen.flutter.image.renderer.provider;

import org.axen.flutter.image.renderer.entity.NativeImage;

public interface ImageProvider<T> {
    T provide(NativeImage info) throws Exception;
}
