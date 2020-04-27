import 'package:flutter/material.dart';
import 'package:share/share.dart';
import 'package:share_with_test/aligator_model.dart';

class HomePage extends StatelessWidget {
  List<Alligator> _aligators = [
    Alligator(
        name: 'Pedro',
        description: 'Galinhas e animais jovens que gostam de beber na lagoa'),
    Alligator(name: 'Ronbrinsonst', description: 'Passarinhos muito loucos'),
    Alligator(name: 'Celelolra', description: 'q tiver ai'),
  ];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Test'),
      ),
      body: Column(
        children: _aligators
            .map(
              (Alligator alligator) => Card(
                child: ListTile(
                  title: Text(alligator.name),
                  subtitle: Text(alligator.description),
                  onTap: () => share(context, alligator),
                ),
              ),
            )
            .toList(),
      ),
    );
  }

  void share(BuildContext context, Alligator alligator) {
    final RenderBox box = context.findRenderObject();
    final String text = '${alligator.name} - ${alligator.description}';

    Share.share(text,
        subject: alligator.description,
        sharePositionOrigin: box.globalToLocal(Offset.zero) & box.size);
  }
}
