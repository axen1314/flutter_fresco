package org.axen.flutter.fresco.provider;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSources;
import com.facebook.drawee.backends.pipeline.DefaultDrawableFactory;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.drawable.DrawableFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.axen.flutter.texture.entity.NativeImage;
import org.axen.flutter.texture.uri.URIParser;

public class FrescoDrawableProvider extends NativeImageProvider<Drawable> {
    private final DrawableFactory mDrawableFactory;


    public FrescoDrawableProvider(Context context) {
        super(context);
        ImagePipelineFactory pipelineFactory = ImagePipelineFactory.getInstance();
        DrawableFactory animatedDrawableFactory = pipelineFactory.getAnimatedDrawableFactory(context);
        mDrawableFactory = new DefaultDrawableFactory(context.getResources(), animatedDrawableFactory);
    }

    @Override
    public Drawable provide(NativeImage info) throws Throwable {
        URIParser parser = parsers.get(info.getSourceType());
        if (parser == null) throw new RuntimeException("Not support resource type!");
        Drawable drawable = fetchDrawableFromFresco(parser.parse(info.getSource()));
        if (drawable == null) throw new NetworkErrorException("Failed to load Image!");
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return drawable;
    }

    private Drawable fetchDrawableFromFresco(Uri uri) throws Throwable {
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest request = builder.build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Drawable drawable;
        drawable = fetchDrawableFromDataSource(imagePipeline.fetchImageFromBitmapCache(request, context));
        if (drawable == null) drawable = fetchDrawableFromDataSource(imagePipeline.fetchDecodedImage(request, context));
        return drawable;
    }

    private Drawable fetchDrawableFromDataSource(DataSource<CloseableReference<CloseableImage>> dataSource) throws Throwable {
        try {
            CloseableReference<CloseableImage> imageReference = DataSources.waitForFinalResult(dataSource);
            try {
                if (imageReference != null) {
                    CloseableImage image = imageReference.get();
                    if (mDrawableFactory.supportsImageType(image)) {
                        return mDrawableFactory.createDrawable(image);
                    }
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
