package org.axen.flutter.fresco;

import android.content.Context;

import org.axen.flutter.fresco.renderer.FrescoSurfaceImageRenderer;
import org.axen.flutter.texture.FlutterTexturePlugin;
import org.axen.flutter.texture.constant.SourceType;
import org.axen.flutter.texture.renderer.ImageRenderer;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.view.TextureRegistry;

/** FlutterFrescoPlugin */
public class FlutterFrescoPlugin extends FlutterTexturePlugin implements FlutterPlugin {
  private static final String CHANNEL = "org.axen.flutter/flutter_fresco";

  @Override
  protected ImageRenderer getImageRenderer(Context context, TextureRegistry.SurfaceTextureEntry entry, SourceType sourceType) {
    return new FrescoSurfaceImageRenderer(context, entry);
  }

  @Override
  protected String getChannel() {
    return CHANNEL;
  }
}
