class ToDo {
  late String _description;
  bool _done = false;

  ToDo(this._description);

  String get description => _description;
  
  bool get isDone => _done; 
  set setDone(bool done) => _done = done; 

  //Convert to and from JSON
  Map<String, dynamic> toJson() => {
    'description':_description,
    'done':_done,
  };

  ToDo.fromJson(Map<String, dynamic> json)
    : _description = json['description'],
      _done = json['done'];
}