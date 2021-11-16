import 'dart:io';

import 'package:flutter/cupertino.dart' hide ImageProvider;
import 'package:flutter/material.dart' hide ImageProvider;
import 'package:flutter/services.dart';
import 'package:flutter_fresco/entities.dart';

import 'providers.dart';

class Fresco extends StatefulWidget {
  /// Loading组件
  final Widget? placeholder;
  /// 图片宽度
  final double width;
  /// 图片高度
  final double height;
  /// 图片展示模式
  final BoxFit fit;
  /// 图片内间距
  final EdgeInsetsGeometry padding;
  /// 图片外边距
  final EdgeInsetsGeometry margin;
  /// 图片资源提供者
  final ImageProvider image;
  /// 图片渲染质量，默认为低质量
  /// 注：设置为高质量会有BUG，默认情况为低质量
  final FilterQuality filterQuality;

  Fresco.network(String url, {
    Key? key,
    this.placeholder,
    required this.width,
    required this.height,
    this.fit: BoxFit.contain,
    this.padding: EdgeInsets.zero,
    this.margin: EdgeInsets.zero,
    this.filterQuality: FilterQuality.medium,
  }): this.image = NetworkImageProvider(url);

  Fresco.file(File file, {
    Key? key,
    this.placeholder,
    required this.width,
    required this.height,
    this.fit: BoxFit.contain,
    this.padding: EdgeInsets.zero,
    this.margin: EdgeInsets.zero,
    this.filterQuality: FilterQuality.low,
  }): this.image = FileImageProvider(file);

  Fresco.drawable(String drawable, {
    Key? key,
    this.placeholder,
    required this.width,
    required this.height,
    this.fit: BoxFit.contain,
    this.padding: EdgeInsets.zero,
    this.margin: EdgeInsets.zero,
    this.filterQuality: FilterQuality.low,
  }): this.image = DrawableImageProvider(drawable);

  Fresco.asset(String asset, {
    Key? key,
    this.placeholder,
    required this.width,
    required this.height,
    this.fit: BoxFit.contain,
    this.padding: EdgeInsets.zero,
    this.margin: EdgeInsets.zero,
    this.filterQuality: FilterQuality.low,
  }): this.image = AssetImageProvider(asset);

  const Fresco(this.image, {
    Key? key,
    this.placeholder,
    required this.width,
    required this.height,
    this.fit: BoxFit.contain,
    this.padding: EdgeInsets.zero,
    this.margin: EdgeInsets.zero,
    this.filterQuality: FilterQuality.low,
  }) : super(key: key);

  @override
  State<StatefulWidget> createState() => _FrescoState();
}

class _FrescoState extends State<Fresco> {
  NativeImageResult? _imageResult;
  NativeImage? _prevImage;

  static const MethodChannel _channel = const MethodChannel('org.axen.flutter/flutter_fresco');

  @override
  void initState() {
    super.initState();
    _loadImage(_image);
  }

  @override
  void didUpdateWidget(covariant Fresco oldWidget) {
    super.didUpdateWidget(oldWidget);
    final NativeImage image = _image;
    if (_prevImage != null && _prevImage != image) {
      _loadImage(image, textureId: _imageResult!.textureId);
    }
  }

  @override
  void dispose() {
    super.dispose();
    _channel.setMethodCallHandler(null);
  }


  @override
  Widget build(BuildContext context) {
    Widget child;
    if (_imageResult == null) {
      child = widget.placeholder??Container();
    } else {
      child = Container(
        width: _imageResult?.size.width??0,
        height: _imageResult?.size.height??0,
        child: Texture(textureId: _imageResult!.textureId),
      );
    }
    return Container(
      width: widget.width,
      height: widget.height,
      padding: widget.padding,
      margin: widget.margin,
      child: ClipRect(
          child: FittedBox(
            alignment: Alignment.center,
            fit: widget.fit,
            child: child,
          )
      ),
    );
  }

  void _loadImage(final NativeImage image, {int? textureId}) {
    Map<String, dynamic> data = image.toMap();
    _channel.invokeMethod("load", data..['textureId'] = textureId)
        .then((value) {
      setState(() {
        _prevImage = image;
        _imageResult = NativeImageResult(
            value["textureId"],
            _caculateImageWidgetSize(value["width"], value["height"])
        );
      });
    });
  }

  /// 计算图片显示的宽高
  /// @param width 图片真实宽度
  /// @param height 图片真实高度
  Size _caculateImageWidgetSize(int w, int h) {
    final double cWidth = widget.width;
    final double cHeight = widget.height;
    final double width = w * 1.0;
    final double height = h * 1.0;
    if ((width <= cWidth && height <= cHeight)) {
      return Size(width, height);
    }
    if (widget.fit == BoxFit.fill) {
      return Size(cWidth, cHeight);
    }
    if (widget.fit == BoxFit.fitWidth
        || (width <= height && widget.fit == BoxFit.cover)
        || (width >= height && (widget.fit == BoxFit.scaleDown || widget.fit == BoxFit.contain))
    ) {
      double wRatio = cWidth / width;
      double iHeight = height * wRatio;
      return Size(cWidth, iHeight);
    }
    if (widget.fit == BoxFit.fitHeight
        || (width > height && widget.fit == BoxFit.cover)
        || (width < height && (widget.fit == BoxFit.scaleDown || widget.fit == BoxFit.contain))
    ) {
      double hRatio = cHeight / height;
      double iWidth = width * hRatio;
      return Size(iWidth, cHeight);
    }
    return Size(width, height);
  }

  NativeImage get _image => widget.image.resolve()
    ..fit = widget.fit
    ..width = widget.width
    ..height = widget.height;
}


