package org.axen.flutter.fresco;

import android.content.Context;

import androidx.annotation.NonNull;

import org.axen.flutter.fresco.renderer.FrescoSurfaceImageRenderer;
import org.axen.flutter.image.renderer.ImageRenderer;
import org.axen.flutter.image.renderer.constant.BoxFit;
import org.axen.flutter.image.renderer.constant.SourceType;
import org.axen.flutter.image.renderer.entity.NativeImage;

import java.util.HashMap;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.view.TextureRegistry;

/** FlutterFrescoPlugin */
public class FlutterFrescoPlugin implements FlutterPlugin, MethodCallHandler {
  private static final String CHANNEL = "org.axen.flutter/flutter_fresco";

  private Context context;
  private MethodChannel channel;
  private TextureRegistry textureRegistry;
  private Map<Integer, ImageRenderer<?>> rendererMap;

  public void onAttachedToEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
    rendererMap = new HashMap<>();
    context = binding.getApplicationContext();
    textureRegistry = binding.getTextureRegistry();
    channel = new MethodChannel(binding.getBinaryMessenger(), CHANNEL);
    channel.setMethodCallHandler(this);
  }

  public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    if (call.method.equals("load")) {
      load(call, result);
    } else {
      result.notImplemented();
    }
  }

  private void load(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
    Integer textureId = call.argument("textureId");
    // TODO 支持OPENGL ES渲染
    ImageRenderer<?> renderer;
    if (textureId == null || !rendererMap.containsKey(textureId)) {
      TextureRegistry.SurfaceTextureEntry entry = textureRegistry.createSurfaceTexture();
      renderer = new FrescoSurfaceImageRenderer(context, entry);
      rendererMap.put((int) entry.id(), renderer);
    } else {
      renderer = rendererMap.get(textureId);
    }
    NativeImage info = NativeImage.obtain();
    info.setSource(call.argument("resource"));
    Integer resourceType = call.argument("resourceType");
    if (resourceType != null)
      info.setSourceType(SourceType.values()[(int) resourceType]);
    Double scaleRatio = call.argument("scaleRatio");
    if (scaleRatio != null) info.setScaleRatio((double) scaleRatio);
    Double width = call.argument("width");
    if (width != null) info.setWidth(width.intValue());
    Double height = call.argument("height");
    if (height != null) info.setHeight(height.intValue());
    Integer fit = call.argument("fit");
    if (fit != null) info.setFit(BoxFit.values()[(int) fit]);
    if (renderer != null) renderer.render(info, result);
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPlugin.FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
    for(Map.Entry<Integer, ImageRenderer<?>> entry : rendererMap.entrySet())
      entry.getValue().release();
    rendererMap.clear();
  }
}
