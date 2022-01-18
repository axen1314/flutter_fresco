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
        body: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              // Image.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 400, height: 400),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.network(url, width: 200, height: 200),
              Fresco.drawable("R.drawable.ic_bar_back", width: 24, height: 24)
            ],
          ),
        ),
      ),
    );
  }
}
