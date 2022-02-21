package org.axen.flutter.fresco.provider;

import android.content.Context;
import android.graphics.drawable.Drawable;

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

abstract class NativeImageProvider<T> implements ImageProvider<T, NativeImage> {

    protected final Map<SourceType, URIParser> parsers;
    protected final Context context;

    public NativeImageProvider(Context context) {
        this.context = context;
        parsers = new HashMap<>();
        parsers.put(SourceType.NETWORK, new NetworkURIParser());
        parsers.put(SourceType.DRAWABLE, new DrawableURIParser(this.context));
        parsers.put(SourceType.ASSET, new AssetURIParser());
        parsers.put(SourceType.FILE, new FileURIParser());
    }
}
