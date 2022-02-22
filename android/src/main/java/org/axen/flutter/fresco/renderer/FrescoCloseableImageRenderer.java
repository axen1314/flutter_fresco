package org.axen.flutter.fresco.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.DefaultDrawableFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.drawable.DrawableFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;

import org.axen.flutter.texture.entity.NativeImage;
import org.axen.flutter.texture.provider.ImageProvider;
import org.axen.flutter.texture.renderer.SurfaceImageRenderer;

import io.flutter.view.TextureRegistry;

public class FrescoCloseableImageRenderer extends SurfaceImageRenderer<CloseableReference<CloseableImage>> {
    private final Context mContext;
    private DrawableFactory mDrawableFactory;
    private Drawable.Callback mCallback;

    public FrescoCloseableImageRenderer(
            Context context,
            TextureRegistry.SurfaceTextureEntry textureEntry,
            ImageProvider<CloseableReference<CloseableImage>, NativeImage> provider
    ) {
        super(textureEntry, provider);
        this.mContext = context;
    }

    @Override
    protected Rect getImageSize(CloseableReference<CloseableImage> image, NativeImage info) {
        CloseableImage img = image.get();
        return new Rect(0, 0, img.getWidth(), img.getHeight());
    }

    @Override
    public void draw(Surface surface, CloseableReference<CloseableImage> image, Handler handler) {
        CloseableImage img = image.get();
        if (img instanceof CloseableStaticBitmap) {
            drawStaticImage(surface, (CloseableStaticBitmap) img);
        } else {
            drawAnimatedImage(surface, img, handler);
        }
        image.close();
    }

    private void drawStaticImage(Surface surface, CloseableStaticBitmap image) {
        Rect dstRect = new Rect(0, 0, image.getWidth(), image.getHeight());
        Canvas canvas = surface.lockCanvas(null);
        // Fixed: PNG图片背景默认显示为白色的问题
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(image.getUnderlyingBitmap(), null, dstRect, null); //图片的绘制
        surface.unlockCanvasAndPost(canvas);
    }

    private void ensureDrawableFactoryInitialize() {
        if (mDrawableFactory == null) {
            ImagePipelineFactory pipelineFactory = ImagePipelineFactory.getInstance();
            DrawableFactory animatedDrawableFactory = pipelineFactory.getAnimatedDrawableFactory(mContext);
            mDrawableFactory = new DefaultDrawableFactory(mContext.getResources(), animatedDrawableFactory);
        }
    }

    private void drawAnimatedImage(final Surface surface, CloseableImage image, final Handler handler) {
        ensureDrawableFactoryInitialize();
        // play drawable animation;
        // keep this reference or JVM might recycle it.
        Drawable drawable = mDrawableFactory.createDrawable(image);
        if (drawable == null) throw new NullPointerException("Fail to load image!");
        mCallback = new Drawable.Callback() {
            @Override
            public void invalidateDrawable(@NonNull Drawable who) {
                if (surface.isValid()) {
                    Canvas canvas = surface.lockCanvas(null);
                    // Fixed: PNG图片背景默认显示为白色的问题
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    who.draw(canvas);
                    surface.unlockCanvasAndPost(canvas);
                }
            }

            @Override
            public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
                handler.postAtTime(what, who, when);
            }

            @Override
            public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
                handler.removeCallbacks(what, who);
            }
        };
        drawable.setCallback(mCallback);
        ((Animatable)drawable).start();
    }

    @Override
    public void release() {
        super.release();
        mCallback = null;
    }
}
