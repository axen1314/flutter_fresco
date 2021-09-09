package org.axen.flutter.image.renderer.uri;

import android.net.Uri;

public class AssetURIParser implements URIParser {

    @Override
    public Uri parse(Object source) {
        return Uri.parse("asset://" + source.toString());
    }
}
