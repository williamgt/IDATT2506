import 'package:flutter/material.dart';
import 'package:todo_list/todo.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusManager.instance.primaryFocus?.unfocus(), //TODO May need some work idk
      child: MaterialApp(
        title: 'ToDo List',
        home: Scaffold(
          appBar: AppBar(
            title: const Text('ToDo List'),
          ),
          body: const Center(
            child: ToDoList(),
          ),
        ),
      ),
    );
  }
}

class ToDoList extends StatefulWidget {
  const ToDoList({super.key});

  @override
  State<ToDoList> createState() => _ToDoListState();
}

class _ToDoListState extends State<ToDoList> {
  final _todos = <ToDo>[ToDo("Halla"), ToDo("Balla"), ToDo("Kronk")];

  final TextEditingController eCtrl = TextEditingController();
  final FocusNode fNode = FocusNode();

  final _biggerFont = const TextStyle(fontSize: 18);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
          child: TextField(
            controller: eCtrl,
            focusNode: fNode,
            onSubmitted: (value) {
              _todos.add(ToDo(value));
              eCtrl.clear(); // Clear the Text area
              fNode.requestFocus();
              setState(() {}); // Redraw the Stateful Widget
            },
            decoration: const InputDecoration(
              hintText: 'Enter something to do',
            ),
          ),
        ),
        Expanded(
          child: ListView.builder(
            //Consider using cards, maybe better if goind to change bg color
            itemCount: _todos.length,
            padding: const EdgeInsets.all(16.0),
            itemBuilder: (context, index) {
              return ListTile(
                title: Text(
                  _todos[index].description,
                  style: _biggerFont,
                ),
              );
            },
            /*  separatorBuilder: (BuildContext context, int index) =>
                const Divider(
              thickness: 1.0,
            ), */
          ),
        )
      ],
    );
  }
}
