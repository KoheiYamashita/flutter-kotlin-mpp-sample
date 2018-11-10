import 'dart:ui';
import 'package:flutter/services.dart';
import 'package:flutter/material.dart';
import 'package:scoped_model/scoped_model.dart';

const platform = const MethodChannel('info.kitproject.flutterkotlin');

class CounterModel extends Model {
  int _counter = 0;
  bool _loading = true;

  CounterModel() {
    load();
  }

  int get counter => _counter;

  bool get loading => _loading;

  void load() async {
    // delay 1 seconds
    await new Future.delayed(new Duration(seconds: 1));
    _counter = await platform.invokeMethod('load');
    _loading = false;
    notifyListeners();
  }

  void increment() async {
    if (_loading) return;
    _loading = true;
    notifyListeners();
    // delay 1 seconds
    await new Future.delayed(new Duration(seconds: 1));
    _counter = await platform.invokeMethod('increment');
    _loading = false;
    notifyListeners();
  }
}

void main() {
  runApp(_widgetForRoute(window.defaultRouteName));
}

Widget _widgetForRoute(String route) {
  switch (route) {
    case 'main':
      return ScopedModel<CounterModel>(
          model: CounterModel(), child: new MyApp());
    default:
      return Center(
        child: Text('Unknown route: $route', textDirection: TextDirection.ltr),
      );
  }
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text("You have pushed the button this many times:"),
            ScopedModelDescendant<CounterModel>(
              builder: (context, child, model) {
                return Text(
                  model.loading ? "loading" : model.counter.toString(),
                  style: Theme.of(context).textTheme.display1,
                );
              },
            ),
          ],
        ),
      ),
      floatingActionButton: ScopedModelDescendant<CounterModel>(
        builder: (context, child, model) {
          return FloatingActionButton(
            onPressed: model.increment,
            tooltip: 'Increment',
            child: Icon(Icons.add),
          );
        },
      ),
    );
  }
}
