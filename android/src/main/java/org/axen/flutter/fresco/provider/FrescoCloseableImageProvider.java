package org.axen.flutter.fresco.provider;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSources;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.axen.flutter.texture.entity.NativeImage;
import org.axen.flutter.texture.uri.URIParser;

public class FrescoCloseableImageProvider extends NativeImageProvider<CloseableReference<CloseableImage>>{

    public FrescoCloseableImageProvider(Context context) {
        super(context);
    }

    @Override
    public CloseableReference<CloseableImage> provide(NativeImage info) throws Throwable {
        URIParser parser = parsers.get(info.getSourceType());
        if (parser == null) throw new RuntimeException("Not support resource type!");
        return fetchDrawableFromFresco(parser.parse(info.getSource()));
    }

    private CloseableReference<CloseableImage> fetchDrawableFromFresco(Uri uri) throws Throwable {
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest request = builder.build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, context);
        try {
            return DataSources.waitForFinalResult(dataSource);
        } finally {
            dataSource.close();
        }
    }
}
