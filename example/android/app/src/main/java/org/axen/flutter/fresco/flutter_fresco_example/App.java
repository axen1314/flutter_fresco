package org.axen.flutter.fresco.flutter_fresco_example;

import android.app.Application;

import com.facebook.animated.giflite.GifDecoder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImageTranscoderType;
import com.facebook.imagepipeline.core.MemoryChunkType;
import com.facebook.imagepipeline.decoder.ImageDecoderConfig;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageDecoderConfig decoderConfig = ImageDecoderConfig.newBuilder()
                .overrideDecoder(DefaultImageFormats.GIF, new GifDecoder())
                .build();
        ImagePipelineConfig pipelineConfig = ImagePipelineConfig.newBuilder(this)
                .setMemoryChunkType(MemoryChunkType.BUFFER_MEMORY)
                .setImageTranscoderType(ImageTranscoderType.JAVA_TRANSCODER)
                .setImageDecoderConfig(decoderConfig)
                .experiment()
                .setNativeCodeDisabled(true)
                .build();
        Fresco.initialize(this, pipelineConfig);
    }
}
