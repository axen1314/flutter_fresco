package org.axen.flutter.fresco;

import android.content.Context;

import org.axen.flutter.fresco.provider.FrescoCloseableImageProvider;
import org.axen.flutter.fresco.renderer.FrescoCloseableImageRenderer;
import org.axen.flutter.texture.FlutterTexturePlugin;
import org.axen.flutter.texture.constant.SourceType;
import org.axen.flutter.texture.entity.NativeImage;
import org.axen.flutter.texture.renderer.ImageRenderer;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.view.TextureRegistry;

/** FlutterFrescoPlugin */
public class FlutterFrescoPlugin extends FlutterTexturePlugin implements FlutterPlugin {
  private static final String CHANNEL = "org.axen.flutter/flutter_fresco";

  @Override
  protected ImageRenderer<NativeImage> getImageRenderer(
          Context context, 
          TextureRegistry.SurfaceTextureEntry entry, SourceType sourceType
  ) {
    return new FrescoCloseableImageRenderer(context, entry, new FrescoCloseableImageProvider(context));
  }

  @Override
  protected String getChannel() {
    return CHANNEL;
  }
}
