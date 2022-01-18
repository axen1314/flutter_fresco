import 'package:flutter/material.dart';

enum SourceType { NETWORK, DRAWABLE, FILE, ASSET }

class NativeImage {
  int? _textureId;
  final String _source;
  final SourceType _sourceType;
  double? _width;
  double? _height;
  BoxFit _fit;

  NativeImage(this._source, this._sourceType, {
    BoxFit fit: BoxFit.contain,
  }): _fit = fit;

  set textureId(value) {
    this._textureId = value;
  }
  get textureId => _textureId;
  set fit(value) {
    this._fit = value;
  }
  get fit => _fit;
  set width(value) {
    this._width = value;
  }
  get width => _width;
  set height(value) {
    this._height = _height;
  }
  get height => _height;
  get source => _source;
  get sourceType => _sourceType;

  Map<String, dynamic> toMap() => {
    "textureId": _textureId,
    "source": _source,
    "sourceType": _sourceType.index,
    "fit": _fit.index
  };

  @override
  bool operator ==(Object other) {
    if (!(other is NativeImage)) return false;
    return source == other.source
        && sourceType == other.sourceType;
  }

  @override
  int get hashCode => super.hashCode;

}

class NativeImageResult {
  final int textureId;
  final Size size;

  NativeImageResult(this.textureId, this.size);
}