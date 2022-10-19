import 'package:flutter/material.dart';
import 'package:todo_list/todo.dart';
import 'package:todo_list/todo_list.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () => FocusManager.instance.primaryFocus
          ?.unfocus(), //TODO May need some work idk
      child: MaterialApp(
        title: 'ToDo List',
        home: Scaffold(
          appBar: AppBar(
            centerTitle: true,
            title: const Text('ToDo List'),
          ),
          body: const Center(
            child: ToDoListWidget(),
          ),
        ),
      ),
    );
  }
}

class ToDoListWidget extends StatefulWidget {
  const ToDoListWidget({super.key});

  @override
  State<ToDoListWidget> createState() => _ToDoListWidgetState();
}

class _ToDoListWidgetState extends State<ToDoListWidget> {
  final _todos = <ToDo>[ToDo("Halla"), ToDo("Balla"), ToDo("Kronk")];
  final _todoLists = <ToDoList>[
    ToDoList('List1', 1),
    ToDoList('List2', 2),
    ToDoList('List with verrry long name', 3),
    ToDoList('Li', 4)
  ];
  int _selectedListIndex = -1;

  final TextEditingController eCtrl = TextEditingController();
  final FocusNode fNode = FocusNode();

  final _biggerFont = const TextStyle(fontSize: 18);

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        SizedBox(
            height: 60,
            child: Row(
              children: <Widget>[
                //Button or adding more lists
                Container(
                  width: 20,
                  margin: const EdgeInsets.all(10),
                  child: IconButton(
                    padding: EdgeInsets.zero,
                    onPressed: ()  { 
                      final prom =  _addToDoListDialog(context); 
                      prom.then((value) => print(value)); //Got value of new name here, persist new list, add it to _todoLists, change _todos and change _selectedListIndex
                      },
                    icon: const Icon(Icons.add))),
                //List of todo lists
                Expanded(
                  child: ListView.builder(
                      itemCount: _todoLists.length,
                      scrollDirection: Axis.horizontal,
                      itemBuilder: ((context, index) {
                        //calculating width of list elements
                        double w = _todoLists[index].title.length * 10 + 2 * 18; //10 per character and add offset equal to font size
                        if (w < 100) w += 30; //If w is small, add more hoping it looks good
                        return Card(
                          child: Container( //Need container cuz listtile takes too much space
                            width: w,
                            child: ListTile(
                            //X button at end of todo list
                              trailing: Container(
                                //Need container cuz iconbutton takes too much space
                                width: 20,
                                child: IconButton(
                                  padding: EdgeInsets.zero,
                                  icon: const Icon(Icons.cancel_rounded,),
                                  onPressed: () {
                                    _deleteToDoListDialog(context, _todoLists[index].title).then((value) => print(value)); //Deleting todo list
                                  },
                                )),
                              //Name of list
                              title: Text(
                                textAlign: TextAlign.left,
                                _todoLists[index].title,
                                style: _biggerFont,
                              ),
                              //Selecting a list
                              selected: index == _selectedListIndex,
                              onTap: () {
                                setState(() {
                                  _selectedListIndex = index;
                                });
                            },
                          ),
                        ));
                      })),
                ),
              ],
            )),
        //ToDo input
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
        //ToDo list
        Expanded(
          child: ListView.builder(
            //Consider using cards, maybe better if goind to change bg color
            itemCount: _todos.length,
            padding: const EdgeInsets.all(16.0),
            itemBuilder: (context, index) {
              return ListTile(
                onTap: () {
                  //_todos[index].isDone ? _todos[index].setDone = false : _todos[index].setDone = true;
                  //If element is done when tapped, must change state to undone. Move to top, TODO change in future?
                  if (_todos[index].isDone) {
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
                },
                tileColor: _todos[index].isDone
                    ? const Color.fromARGB(255, 219, 241, 201)
                    : null,
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

enum PopUpResult {cancel, approve, add}

Future _addToDoListDialog(BuildContext context) async {
  String? listName;
  return showDialog(
    context: context,
    //barrierDismissible: false,
    builder: (BuildContext context) {
      return AlertDialog(
        title: const Text('Add ToDo list'),
        content: TextField(
            onSubmitted: (value) {
              print('Submitted');
              Navigator.pop(context, value);
            },
            onChanged: (value) => listName = value,
            decoration: const InputDecoration(
              hintText: 'Enter name of new list',
            ),
        ),
        actions: <Widget>[
          TextButton(
            onPressed: () => Navigator.pop(context, PopUpResult.cancel),
            child: const Text('Cancel'),
            ),
          TextButton(
            child: const Text('Approve'),
            onPressed: () {
              if(listName == null) {
                return;
              }
              else {
                Navigator.pop(context, listName);
              }
            },
          ),
        ],
      );
    },
  );
}

Future<PopUpResult?> _deleteToDoListDialog(BuildContext context, String listName) async {
  return showDialog<PopUpResult>(
    context: context,
    //barrierDismissible: false, 
    builder: (BuildContext context) {
      return AlertDialog(
        title: Text('Delete $listName'),
        content: Text('Are you sure you want to delete $listName?'),
        actions: <Widget>[
          TextButton(
            onPressed: () => Navigator.pop(context, PopUpResult.cancel),
            child: const Text('Cancel'),
          ),
          TextButton(
            child: const Text('Approve'),
            onPressed: () => Navigator.pop(context, PopUpResult.approve),
          ),
        ],
      );
    },
  );
}