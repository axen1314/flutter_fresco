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
  final List<String> urlList = [
    "https://app-static.beieryouxi.com/image/game/2022/02/621355a31f7d37bf23931b14.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620f0eb8b94dc3db7d20c053.png",
    "https://and-static.ghzs.com/image/game/icon/2022/01/28/61f39f6b98be15798540a749.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620f7a26b94dc3db7d21fa77.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/621335501f7d37bf2391cb5c.gif",
    "https://app-static.beieryouxi.com/image/game/2022/02/620f4c86b94dc3db7d213651.png",
    "https://resource.ghzs.com/image/game/icon/5f6884e1d84f28722d1a7b01.png",
    "https://resource.ghzs.com/image/game/icon/5e74821ed5db460a646871f9.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620f6010b94dc3db7d2136b7.gif",
    "https://app-static.beieryouxi.com/image/game/2022/02/620dfa91b94dc3db7d1c93bf.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620df4f8b94dc3db7d1bf71a.gif",
    "https://app-static.beieryouxi.com/image/game/2022/02/620e43fdb94dc3db7d1f831b.gif",
    "https://and-static.ghzs.com/image/game/icon/2022/01/28/61f39f6b98be15798540a749.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620f0c57b94dc3db7d20992f.gif",
    "https://and-static.ghzs.com/image/game/icon/2022/02/21/62132f5f2000a07a6e73187b.gif",
    "https://app-static.beieryouxi.com/image/game/2022/02/620f040ab94dc3db7d1f871a.gif",
    "https://app-static.beieryouxi.com/image/game/2022/02/620da49db94dc3db7d18b606.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620df634b94dc3db7d1bf72c.gif",
    "https://app-static.beieryouxi.com/image/game/2022/02/620ded69b94dc3db7d1a9314.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620b8cbfb94dc3db7d16dadc.png",
    "https://app-static.beieryouxi.com/image/game/2022/02/620a2aa4b94dc3db7d14dd85.gif",
    "https://and-static.ghzs.com/image/game/icon/2022/01/20/61e92af9bb664368d973b750.png",
    "https://and-static.ghzs.com/image/game/icon/2022/01/20/61e92af9bb664368d973b750.png"
  ];

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
        // body: Center(
        //     child: Column(
        //       children: [
        //         Image.network(gifUrl, width: 200, height: 200),
        //         Fresco.network(gifUrl, width: 200, height: 200),
        //         Fresco.drawable("R.drawable.ic_bar_back", width: 24, height: 24)
        //       ],
        //     )
        // ),
        body: ListView.builder(
          itemBuilder: (BuildContext context, int index) {
            List<Widget> children = [];
            for (int i = 0;i < 5;i++) {
              children.add(
                  Fresco.network(
                      urlList[index],
                      width: 64,
                      height: 64
                  )
              );
            }
            return Column(children: children);
          },
          itemCount: urlList.length,
        ),
      ),
    );
  }
}
