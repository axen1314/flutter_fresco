package org.axen.flutter.fresco;

import android.content.Context;

import org.axen.flutter.fresco.provider.FrescoBitmapProvider;
import org.axen.flutter.texture.FlutterTexturePlugin;
import org.axen.flutter.texture.constant.SourceType;
import org.axen.flutter.texture.entity.NativeImage;
import org.axen.flutter.texture.renderer.ImageRenderer;
import org.axen.flutter.texture.renderer.SurfaceBitmapRenderer;

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
    return new SurfaceBitmapRenderer(entry, new FrescoBitmapProvider(context));
  }

  @Override
  protected String getChannel() {
    return CHANNEL;
  }
}
