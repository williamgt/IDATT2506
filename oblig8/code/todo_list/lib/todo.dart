class ToDo {
  late String _description;
  bool _done = false;

  ToDo(this._description);

  String get description => _description;
  
  bool get isDone => _done; 
  set setDone(bool done) => _done = done; 

      @override
    String toString() {
        return '$_description $_done'; 
    }
}