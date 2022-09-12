package no.ntnu.idatt2506.stud.williagt.register_friend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class EditFriend : AppCompatActivity() {
    private val TAG = "EDIT"

    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_friend)

        val startIntent = intent
        position = startIntent.getIntExtra("position", -1)
        val person = startIntent.getSerializableExtra("person") as Person

        findViewById<EditText>(R.id.editTextEditName).setText(person.name)
        findViewById<EditText>(R.id.editTextEditBirthday).setText(person.birthday)
    }

    fun onClickButtonCancel(view: View) {
        Log.d(TAG, "Cancel editing friend...")
        setResult(RESULT_CANCELED)
        finish()
    }

    fun onClickButtonOK(view: View) {
        val name = findViewById<EditText>(R.id.editTextEditName).text.toString()
        val birthday = findViewById<EditText>(R.id.editTextEditBirthday).text.toString()
        if(name.isEmpty() || birthday.isEmpty()) return

        Log.d(TAG, "Editing friend $name born at $birthday")
        val data = Intent()
        data.putExtra("position", position)
        data.putExtra("editedPerson", Person(name, birthday))
        setResult(RESULT_OK, data)
        finish()
    }
}