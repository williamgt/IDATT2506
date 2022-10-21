import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:todo_list/file_handler.dart';
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
          ?.unfocus(), 
      child: MaterialApp(
        title: 'ToDo List',
        home: Scaffold(
          appBar: AppBar(
            centerTitle: true,
            title: const Text('ToDo List'),
          ),
          body: const Center(
            child: MyToDoList(),
          ),
        ),
      ),
    );
  }
}

class MyToDoList extends StatefulWidget {
  const MyToDoList({super.key});

  @override
  State<MyToDoList> createState() => _MyToDoListState();
}

class _MyToDoListState extends State<MyToDoList> {
  final _todoLists = <ToDoList>[];
  int _selectedListIndex = -1;

  final TextEditingController eCtrl = TextEditingController();
  final FocusNode fNode = FocusNode();

  final _biggerFont = const TextStyle(fontSize: 18);

  @override
  void initState() {
    super.initState();

    FileHandler.readIndicies().then((value) { //Getting all ids of saved todo lists
      try{
        var ids = List<int>.from(json.decode(value!)); 
        for (var id in ids) { //Looping through ids of all saved data
          FileHandler.readJsonFileById(id).then((value) { //Reading todo list by id
            if(value != null){
              var toDoList = ToDoList.fromJson(jsonDecode(value)); //Converting from json to todo list
              setState(() { //Need to call this to get flutter to understand that something was added to _todoLists
                _todoLists.add(toDoList); //Adding todo list to _todoLists
              }); 
            }
          });
        }
      }
      catch (e) { //Tried to decode null value, meaning no file named indicies exists. Creating one.
        FileHandler.writeIndicies([]);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        SizedBox(
          height: 60,
          child: Row(
            children: <Widget>[
              //Button for adding more lists
              Container(
                width: 20,
                margin: const EdgeInsets.all(10),
                child: IconButton(
                  padding: EdgeInsets.zero,
                  onPressed: ()  { 
                    var ids = _todoLists.map((element) => element.id);
                    //Got value of new name here, persist new list, add it to _todoLists, change _todos and change _selectedListIndex
                    _addToDoListDialog(context).then((value) {
                      if(value is String) {
                        var id = _todoLists.length;
                        for (var e in _todoLists) { //Trying to get unique id
                          if(e.id == id) id++;
                        }
                        var newList = ToDoList(value, id); //Maybe have unique 
                        if(_todoLists.contains(newList)) {
                          throw Exception('List ID already in use, something really wrong happened'); //If this ever happens, consider using some random string as id instead
                        }
                        //Writing new list to file
                        FileHandler.writeJsonDataWithId(newList.toJson(), newList.id);
                        //Updating ui
                        setState(() {
                          _todoLists.add(newList);
                          _selectedListIndex = _todoLists.length - 1;
                        });
                        //Writing new indicies to file
                        FileHandler.writeIndicies(_todoLists.map((element) => element.id).toList());
                      } 
                    });
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
                    while (w < 100) w += 20.0; //If w is small, add more hoping it looks good
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
                                _deleteToDoListDialog(context, _todoLists[index].title).then((value) {
                                  if(value == PopUpResult.approve) { //Deletion of todo list was approved
                                    ToDoList deleted = _todoLists.removeAt(index); //Removing list
                                    if(_selectedListIndex >= index)_selectedListIndex = index - 1; //Updating selected index if it's necessary 
                                    setState(() {}); //Updating ui
                                    FileHandler.deleteFile(deleted.id); //Deleting todo list file
                                    FileHandler.writeIndicies(_todoLists.map((element) => element.id).toList()); //Updating indicies
                                  }
                                }); 
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
        //ToDo input, renders only if a todo list is selected
        if(_selectedListIndex > -1 && _selectedListIndex <= _todoLists.length)
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 16),
          child: TextField(
            controller: eCtrl,
            focusNode: fNode,
            onSubmitted: (value) {
              setState(() {
                if(value.isNotEmpty) {
                  _todoLists[_selectedListIndex].todos.insert(0, ToDo(value)); // Add to start of _todos
                  FileHandler.writeJsonDataWithId(_todoLists[_selectedListIndex].toJson(), _todoLists[_selectedListIndex].id); //Saving changes
                }
              }); // Redraw the Stateful Widget
              eCtrl.clear(); // Clear the Text area
              fNode.requestFocus(); // Put focus back on keyboard  
            },
            decoration: const InputDecoration(
              hintText: 'Enter something to do',
            ),
          ),
        ),
        //ToDo list, renders only if a todo list is selected
        if(_selectedListIndex > -1 && _selectedListIndex <= _todoLists.length)
        Expanded(
          child: ListView.builder(
            //Consider using cards, maybe better if goind to change bg color
            itemCount: _todoLists[_selectedListIndex].toDoAmount,
            padding: const EdgeInsets.all(16.0),
            itemBuilder: (context, index) {
              return ListTile(
                onTap: () {
                  //If element is done when tapped, must change state to undone. Move to top, TODO change in future?
                  if (_todoLists[_selectedListIndex].todos[index].isDone) {
                    setState(() {
                      _todoLists[_selectedListIndex].setToDoDone(index, false);
                      var todo = _todoLists[_selectedListIndex].removeToDoAt(index);
                      _todoLists[_selectedListIndex].todos.insert(0, todo);
                    });
                  }
                  //If it is not done, change state to done and move to the bottom
                  else {
                    setState(() {
                      _todoLists[_selectedListIndex].setToDoDone(index, true);
                      var todo = _todoLists[_selectedListIndex].removeToDoAt(index);
                      _todoLists[_selectedListIndex].todos.add(todo);
                    });
                  }
                  FileHandler.writeJsonDataWithId(_todoLists[_selectedListIndex].toJson(), _todoLists[_selectedListIndex].id); //Saving changes
                },
                tileColor: _todoLists[_selectedListIndex].todos[index].isDone
                    ? const Color.fromARGB(255, 219, 241, 201)
                    : null,
                title: Text(
                  _todoLists[_selectedListIndex].todos[index].description,
                  style: _biggerFont,
                ),
              );
            },
          ),
        )
      ],
    );
  }
}

enum PopUpResult {cancel, approve, add}

Future _addToDoListDialog(BuildContext context) async {
  String listName = '';
  return showDialog(
    context: context,
    //barrierDismissible: false,
    builder: (BuildContext context) {
      return AlertDialog(
        title: const Text('Add ToDo list'),
        content: TextField(
            onSubmitted: (value) {
              Navigator.pop(context, value); //Return list name to prev screen when enter is hit
            },
            onChanged: (value) => listName = value,
            decoration: const InputDecoration(
              hintText: 'Enter name of new list',
            ),
        ),
        actions: <Widget>[
          TextButton(
            child: const Text('Cancel'),
            onPressed: () => Navigator.pop(context, PopUpResult.cancel),
            ),
          TextButton(
            child: const Text('Approve'),
            onPressed: () {
              if(listName.isEmpty) { //Wont approve if name is list name is empty
                return;
              }
              else {
                Navigator.pop(context, listName); //Return list name to prev screen when pressed approve
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
            child: const Text('Cancel'),
            onPressed: () => Navigator.pop(context, PopUpResult.cancel),
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