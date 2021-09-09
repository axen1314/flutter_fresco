package org.axen.flutter.image.renderer;

import static java.lang.reflect.Modifier.PROTECTED;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.view.Surface;

import androidx.annotation.VisibleForTesting;

import org.axen.flutter.image.renderer.entity.NativeImage;
import org.axen.flutter.image.renderer.provider.ImageProvider;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.TextureRegistry;

public abstract class SurfaceImageRenderer<T> extends AbstractImageRenderer<T> {

    public SurfaceImageRenderer(
            TextureRegistry.SurfaceTextureEntry textureEntry,
            ImageProvider<T> provider
    ) {
        super(textureEntry, provider);
    }

    private Surface surface;

    @Override
    public void release() {
        textureEntry.release();
        if (surface != null) surface.release();
    }

    @Override
    protected void onDraw(T image, NativeImage info, MethodChannel.Result result) {
        SurfaceTexture texture = textureEntry.surfaceTexture();
        if (surface == null) surface = new Surface(texture);
        if (surface.isValid()) {
            int[] size = getSize(image, info);
            texture.setDefaultBufferSize(size[0], size[1]);
            draw(surface, image);
            Map<String, Object> map = new HashMap<>();
            map.put("textureId", textureEntry.id());
            double scaleRatio = info.getScaleRatio();
            map.put("width", size[0] / scaleRatio);
            map.put("height", size[1] / scaleRatio);
            postSuccess(result, map);
        } else {
            postError(result, "Surface is invalid!");
        }
    }

    protected abstract int[] getSize(T image, NativeImage info);

    @VisibleForTesting(otherwise = PROTECTED)
    public abstract void draw(Surface surface, T image);
}
