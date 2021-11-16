package org.axen.flutter.fresco.provider;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSources;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.axen.flutter.texture.constant.SourceType;
import org.axen.flutter.texture.entity.NativeImage;
import org.axen.flutter.texture.provider.ImageProvider;
import org.axen.flutter.texture.uri.AssetURIParser;
import org.axen.flutter.texture.uri.DrawableURIParser;
import org.axen.flutter.texture.uri.FileURIParser;
import org.axen.flutter.texture.uri.NetworkURIParser;
import org.axen.flutter.texture.uri.URIParser;

import java.util.HashMap;
import java.util.Map;

public class FrescoBitmapProvider implements ImageProvider<Bitmap, NativeImage> {

    private final Map<SourceType, URIParser> parsers;
    private final Context context;

    public FrescoBitmapProvider(Context context) {
        this.context = context;
        parsers = new HashMap<>();
        parsers.put(SourceType.NETWORK, new NetworkURIParser());
        parsers.put(SourceType.DRAWABLE, new DrawableURIParser(this.context));
        parsers.put(SourceType.ASSET, new AssetURIParser());
        parsers.put(SourceType.FILE, new FileURIParser());
    }


    @Override
    public Bitmap provide(NativeImage info) throws Throwable {
        URIParser parser = parsers.get(info.getSourceType());
        if (parser == null) throw new RuntimeException("Not support resource type!");
        Bitmap bitmap = fetchBitmapFromFresco(parser.parse(info.getSource()));
        if (bitmap == null) throw new NetworkErrorException("Failed to load Image!");
        return bitmap;
    }

    private Bitmap fetchBitmapFromFresco(Uri uri) throws Throwable {
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest request = builder.build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Bitmap bitmap;
        bitmap = fetchBitmapFromDataSource(imagePipeline.fetchImageFromBitmapCache(request, context));
        if (bitmap == null) bitmap = fetchBitmapFromDataSource(imagePipeline.fetchDecodedImage(request, context));
        return bitmap;
    }

    private Bitmap fetchBitmapFromDataSource(DataSource<CloseableReference<CloseableImage>> dataSource) throws Throwable {
        try {
            CloseableReference<CloseableImage> imageReference = DataSources.waitForFinalResult(dataSource);
            try {
                if (imageReference != null) {
                    return ((CloseableBitmap)imageReference.get()).getUnderlyingBitmap();
                }
            } finally {
                CloseableReference.closeSafely(imageReference);
            }
        } finally {
            dataSource.close();
        }
        return null;
    }
}
