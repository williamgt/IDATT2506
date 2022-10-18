class ToDoList {
  String _title;
  int _id;

  ToDoList(this._title, this._id);

  String get title => _title;
  
  int get id => _id; 

  //ToDoLists are equal if they have the same id
  @override
  bool operator ==(Object other) =>
    other is ToDoList &&
    other._id == _id;    
}