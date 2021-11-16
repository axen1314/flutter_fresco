import 'dart:io';

import 'package:flutter_fresco/entities.dart';



/// Drawable图片资源
class DrawableImageProvider extends ImageProvider<String> {
  final String drawable;

  const DrawableImageProvider(this.drawable);

  @override
  NativeImage resolve() => NativeImage(drawable, SourceType.DRAWABLE);
}

/// 文件图片资源
class FileImageProvider extends ImageProvider<File> {
  final File file;

  const FileImageProvider(this.file);

  @override
  NativeImage resolve() => NativeImage(file.path, SourceType.FILE);
}

/// 网络图片资源
class NetworkImageProvider extends ImageProvider<String> {
  final String url;

  const NetworkImageProvider(this.url);

  @override
  NativeImage resolve() => NativeImage(url, SourceType.NETWORK);

}

/// Asset图片资源
class AssetImageProvider extends ImageProvider<String> {
  final String asset;

  const AssetImageProvider(this.asset);
  
  @override
  NativeImage resolve() => NativeImage(asset, SourceType.ASSET);
}

abstract class ImageProvider<T> {
  const ImageProvider();

  NativeImage resolve();
}