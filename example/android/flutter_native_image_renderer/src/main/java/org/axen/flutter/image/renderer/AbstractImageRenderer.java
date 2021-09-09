package org.axen.flutter.image.renderer;

import android.os.Handler;
import android.os.Looper;

import org.axen.flutter.image.renderer.entity.NativeImage;
import org.axen.flutter.image.renderer.provider.ImageProvider;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.TextureRegistry;

public abstract class AbstractImageRenderer<T> implements ImageRenderer<T> {
    protected TextureRegistry.SurfaceTextureEntry textureEntry;
    private final ImageProvider<T> provider;

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public AbstractImageRenderer(
            TextureRegistry.SurfaceTextureEntry textureEntry,
            ImageProvider<T> provider
    ) {
        this.textureEntry = textureEntry;
        this.provider = provider;
    }

    public void render(final NativeImage info, final MethodChannel.Result result) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    T image = provider.provide(info);
                    onDraw(image, info, result);
                } catch (Exception e) {
                    onFail(e);
                    postError(result, e.getMessage());
                } finally {
                    onComplete();
                    info.recycle();
                }
            }
        });
    }

    protected void postSuccess(final MethodChannel.Result result, final Map<String, Object> map) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                result.success(map);
            }
        });
    }

    protected void postError(
            final MethodChannel.Result result,
            final String errorString
    ) {
        postError(result, "-1", errorString);
    }

    protected void postError(
            final MethodChannel.Result result,
            final String errorCode,
            final String errorString
    ) {
        postError(result, errorCode, errorString, "");
    }

    protected void postError(
            final MethodChannel.Result result,
            final String errorCode,
            final String errorString,
            final String errorDetail
    ) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                result.error(errorCode, errorString, errorDetail);
            }
        });
    }

    protected void onComplete() {}

    protected void onFail(Exception error) {}

    protected abstract void onDraw(T image, NativeImage info, MethodChannel.Result result);

}
