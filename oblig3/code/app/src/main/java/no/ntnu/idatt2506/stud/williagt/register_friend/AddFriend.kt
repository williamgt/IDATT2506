package no.ntnu.idatt2506.stud.williagt.register_friend

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class AddFriend : AppCompatActivity() {
    private val TAG = "REGISTER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
    }

    fun onClickButtonCancel(view: View) {
        Log.d(TAG, "Cancel adding friend...")
        setResult(RESULT_CANCELED)
        finish()
    }

    fun onClickButtonAdd(view: View) {
        val name = findViewById<EditText>(R.id.editTextRegisterName).text.toString()
        val birthday = findViewById<EditText>(R.id.editTextRegisterBirthday).text.toString()
        if(name.isEmpty() || birthday.isEmpty()) return

        Log.d(TAG, "Adding friend $name born at $birthday")
        val data = Intent()
        data.putExtra("registeredPerson", Person(name, birthday))
        setResult(RESULT_OK, data)
        finish()
    }
}