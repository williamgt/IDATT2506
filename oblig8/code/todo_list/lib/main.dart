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
              _todos.insert(0, ToDo(value)); // Add to start of _todos
              eCtrl.clear(); // Clear the Text area
              fNode.requestFocus(); // Put focus back on keyboard
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
                onTap: () {
                  //_todos[index].isDone ? _todos[index].setDone = false : _todos[index].setDone = true;
                  //If element is done when tapped, must change state to undone. Move to top, TODO change in future
                  if(_todos[index].isDone) {
                    setState(() {
                      _todos[index].setDone = false;
                      var todo = _todos.removeAt(index);
                      _todos.insert(0, todo);
                    });
                    
                  }
                  //If it is not done, change state to done and move to the bottom
                  else {
                    setState(() {
                     _todos[index].setDone = true;
                     var todo = _todos.removeAt(index);
                     _todos.add(todo);
                    });
                    
                  }
                 // setState(() {}); 
                  },
                tileColor: _todos[index].isDone ? Colors.grey : null,
                title: Text(
                  _todos[index].toString(), //TODO change to correct after debug
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
