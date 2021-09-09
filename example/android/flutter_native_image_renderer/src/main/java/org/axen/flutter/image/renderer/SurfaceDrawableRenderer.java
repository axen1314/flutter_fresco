package org.axen.flutter.image.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;

import org.axen.flutter.image.renderer.entity.NativeImage;
import org.axen.flutter.image.renderer.provider.ImageProvider;

import io.flutter.view.TextureRegistry;

public class SurfaceDrawableRenderer extends SurfaceImageRenderer<Drawable>{

    public SurfaceDrawableRenderer(
            TextureRegistry.SurfaceTextureEntry textureEntry,
            ImageProvider<Drawable> provider
    ) {
        super(textureEntry, provider);
    }

    @Override
    protected int[] getSize(Drawable image, NativeImage info) {
        return new int[] {image.getIntrinsicWidth(), image.getIntrinsicHeight()};
    }

    @Override
    public void draw(final Surface surface, final Drawable image) {
        Rect dstRect = new Rect(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        Canvas canvas = surface.lockCanvas(dstRect);
        // Fixed: PNG图片背景默认显示为白色的问题
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        image.draw(canvas);
        surface.unlockCanvasAndPost(canvas);
    }
}
