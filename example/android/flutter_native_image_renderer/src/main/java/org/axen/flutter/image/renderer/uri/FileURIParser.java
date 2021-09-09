package org.axen.flutter.image.renderer.uri;

import android.net.Uri;

public class FileURIParser implements URIParser {

    @Override
    public Uri parse(Object source) {
        return Uri.parse("file://" + source.toString());
    }
}
