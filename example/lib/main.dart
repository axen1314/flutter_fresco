import 'package:flutter/material.dart';
import 'package:flutter_fresco/flutter_fresco.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String url = "http://img.netbian.com/file/2020/0904/7cab180eca805cce596b6870cb4e1379.jpg";
  String gifUrl = "http://wx2.sinaimg.cn/large/ceeb653ely1fnfri4s099g205004r7rw.gif";

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
            child: Column(
              children: [
                Image.network(gifUrl, width: 200, height: 200),
                Fresco.network(gifUrl, width: 200, height: 200),
                Fresco.drawable("R.drawable.ic_bar_back", width: 24, height: 24)
              ],
            )
        ),
      ),
    );
  }
}
