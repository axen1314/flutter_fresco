package org.axen.flutter.image.renderer;

import static java.lang.reflect.Modifier.PROTECTED;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.view.Surface;

import androidx.annotation.VisibleForTesting;

import org.axen.flutter.image.renderer.entity.NativeImage;
import org.axen.flutter.image.renderer.provider.ImageProvider;

import io.flutter.view.TextureRegistry;

public class SurfaceBitmapRenderer extends SurfaceImageRenderer<Bitmap> {

    public SurfaceBitmapRenderer(
            TextureRegistry.SurfaceTextureEntry textureEntry, 
            ImageProvider<Bitmap> provider
    ) {
        super(textureEntry, provider);
    }

    @Override
    protected int[] getSize(Bitmap image, NativeImage info) {
        return new int[] { image.getWidth(), image.getHeight() };
    }

    @Override
    @VisibleForTesting(otherwise = PROTECTED)
    public void draw(Surface surface, Bitmap image) {
        Rect dstRect = new Rect(0, 0, image.getWidth(), image.getHeight());
        Canvas canvas = surface.lockCanvas(dstRect);
        // Fixed: PNG图片背景默认显示为白色的问题
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(image, null, dstRect, null); //图片的绘制
        surface.unlockCanvasAndPost(canvas);
    }
}
