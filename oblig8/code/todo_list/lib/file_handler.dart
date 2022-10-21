import 'dart:convert';
import 'dart:io';

import 'package:path_provider/path_provider.dart';


class FileHandler {
  static const String _toDoList = 'to-do-list-';
  static const String _indicies = 'indicies';

  static Future<String> get _localPath async {
    final dir = await getApplicationDocumentsDirectory();
    return dir.path;
  }

  static Future<File>  _getLocalFile(String fileName) async {
    final path = await _localPath;
    return File('$path/$fileName');
  }

  static Future<String?> readIndicies() async {
    try {
      final file = await _getLocalFile(_indicies);
      final contents = await file.readAsString();
      return contents;
    } catch (e) {
      return null;
    }
  }

  static Future<File> writeIndicies(List<int> indicies) async {
    final file = await _getLocalFile(_indicies);
    return file.writeAsString(indicies.toString());
  }

  static Future<String?> readJsonFileById(int id) async {
    try {
      final file = await _getLocalFile('$_toDoList$id.json');
      final contents = await file.readAsString();
      return contents;
    } catch (e) {
      return null;
    }
  }

  static Future<File> writeJsonDataWithId(Map<String, dynamic> json, int id) async {
    final file = await _getLocalFile('$_toDoList$id.json');
    return file.writeAsString(jsonEncode(json));
  }

  static Future<int> deleteFile(int id) async {
    try{
      final file = await _getLocalFile('$_toDoList$id.json');
      await file.delete();
      return 0;
    }
    catch (e) {
      return -1;
    }
  }
}