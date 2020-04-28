import 'package:flutter/material.dart';
import 'package:page_stacking_test/first_route.dart';

void main() {
  runApp(HomePage());
}

class HomePage extends StatelessWidget {
  const HomePage({Key key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Route test',
      home: FirstRoute(),
    );
  }
}
