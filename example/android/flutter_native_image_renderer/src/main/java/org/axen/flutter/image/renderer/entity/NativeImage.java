package org.axen.flutter.image.renderer.entity;

import org.axen.flutter.image.renderer.constant.BoxFit;
import org.axen.flutter.image.renderer.constant.SourceType;

public class NativeImage {
    private Object source = null;
    private double scaleRatio = 3.0;
    private int width = 0;
    private int height = 0;
    private BoxFit fit = BoxFit.COVER;
    private SourceType sourceType = SourceType.NONE;

    public static NativeImage obtain() {
        return NativeImagePool.obtain();
    }

    public NativeImage recycle() {
        setWidth(0);
        setHeight(0);
        setSource(null);
        setScaleRatio(3.0);
        setFit(BoxFit.COVER);
        setSourceType(SourceType.NONE);
        return this;
    }

    public final Object getSource() {
        return source;
    }

    public final void setSource(Object source) {
        this.source = source;
    }

    public final double getScaleRatio() {
        return scaleRatio;
    }

    public final void setScaleRatio(double scaleRatio) {
        this.scaleRatio = scaleRatio;
    }

    public final int getWidth() {
        return width;
    }

    public final void setWidth(int width) {
        this.width = width;
    }

    public final int getHeight() {
        return height;
    }

    public final void setHeight(int height) {
        this.height = height;
    }

    public BoxFit getFit() {
        return fit;
    }

    public void setFit(BoxFit fit) {
        this.fit = fit;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
}
