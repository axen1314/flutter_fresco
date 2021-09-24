package org.axen.flutter.fresco.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.axen.flutter.fresco.flutter_fresco.R;
import org.axen.flutter.texture.constant.BoxFit;
import org.axen.flutter.texture.constant.SourceType;
import org.axen.flutter.texture.entity.NativeImage;
import org.axen.flutter.texture.renderer.ImageRenderer;
import org.axen.flutter.texture.uri.AssetURIParser;
import org.axen.flutter.texture.uri.DrawableURIParser;
import org.axen.flutter.texture.uri.FileURIParser;
import org.axen.flutter.texture.uri.NetworkURIParser;
import org.axen.flutter.texture.uri.URIParser;
import org.axen.flutter.texture.utils.NativeImageUtils;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.view.TextureRegistry;

public class FrescoSurfaceImageRenderer implements ImageRenderer<NativeImage> {
    private final Context context;
    private final TextureRegistry.SurfaceTextureEntry textureEntry;
    private final Map<SourceType, URIParser> parsers;
    private Surface surface;
    private final Handler handler = new Handler(Looper.getMainLooper());

    public FrescoSurfaceImageRenderer(final Context context, TextureRegistry.SurfaceTextureEntry textureEntry) {
        this.context = context;
        this.textureEntry = textureEntry;
        parsers = new HashMap<SourceType, URIParser>() {{
            put(SourceType.NETWORK, new NetworkURIParser());
            put(SourceType.DRAWABLE, new DrawableURIParser(context));
            put(SourceType.ASSET, new AssetURIParser());
            put(SourceType.FILE, new FileURIParser());
        }};
    }

    @Override
    public void render(final NativeImage info, final MethodChannel.Result result) {
        URIParser parser = parsers.get(info.getSourceType());
        if (parser == null) throw new RuntimeException("Not support resource type!");
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(parser.parse(info.getSource()));
        final double density = context.getResources().getDisplayMetrics().density;
        final double width = info.getWidth() * density, height = info.getHeight() * density;
        if (width > 0 && height > 0) builder.setResizeOptions(new ResizeOptions((int) width, (int) height));
        ImageRequest request = builder.build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, context);
        try {
            dataSource.subscribe(new BaseBitmapDataSubscriber() {
                @Override
                public void onNewResultImpl(@Nullable Bitmap bitmap) {
                    if (bitmap != null) {
                        SurfaceTexture texture = textureEntry.surfaceTexture();
                        if (surface == null) surface = new Surface(texture);
                        if (surface.isValid()) {
                            texture.setDefaultBufferSize(bitmap.getWidth(), bitmap.getHeight());
                            Rect imageSize = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                            Rect dstRect = NativeImageUtils.calculateImageDstRect(imageSize, info);
                            Rect srcRect = NativeImageUtils.calculateImageSrcRect(imageSize, info, dstRect);
                            draw(surface, bitmap, srcRect);
                            Map<String, Object> map = new HashMap<>();
                            map.put("textureId", textureEntry.id());
                            map.put("width", dstRect.width());
                            map.put("height", dstRect.height());
                            postSuccess(result, map);
                        } else {
                            postError(result,"Surface is invalid!");
                        }
                    } else {
                        postError(result, "Bitmap is null!");
                    }
                    dataSource.close();
                }

                @Override
                public void onFailureImpl(@NonNull DataSource dataSource) {
                    Throwable throwable = dataSource.getFailureCause();
                    String message;
                    if (throwable != null) message = throwable.getMessage();
                    else message = "Fail to fetch decoded image";
                    postError(result, message);
                    dataSource.close();
                }
            }, CallerThreadExecutor.getInstance());
        } catch (Exception e) {
            postError(result, e.getMessage());
            dataSource.close();
        }
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

    @Override
    public void release() {
        parsers.clear();
        textureEntry.release();
        if (surface != null) surface.release();
    }
    public void draw(Surface surface, Bitmap image, Rect srcRect) {
        Rect dstRect = new Rect(0, 0, image.getWidth(), image.getHeight());
        Canvas canvas = surface.lockCanvas(null);
        // Fixed: PNG图片背景默认显示为白色的问题
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(image, srcRect, dstRect, null); //图片的绘制
        surface.unlockCanvasAndPost(canvas);
    }
}
