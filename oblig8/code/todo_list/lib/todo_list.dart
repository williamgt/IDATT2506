import 'package:todo_list/todo.dart';

class ToDoList {
  String _title;
  int _id;
  List<ToDo> _todos = [];

  ToDoList(this._title, this._id);

  String get title => _title;
  
  int get id => _id; 

  List<ToDo> get todos => _todos;
  set setTodos(List<ToDo> todos) => _todos = todos; 

  int get toDoAmount => _todos.length;
  bool toDoIsDone(int index) => _todos[index].isDone;
  void setToDoDone(int index, bool state) => _todos[index].setDone = state;
  ToDo removeToDoAt(int index) => _todos.removeAt(index);
  //ToDoLists are equal if they have the same id
  @override
  bool operator ==(Object other) =>
    other is ToDoList &&
    other._id == _id;    
}