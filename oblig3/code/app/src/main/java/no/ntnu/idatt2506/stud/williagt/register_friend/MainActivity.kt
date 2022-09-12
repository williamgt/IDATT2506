package no.ntnu.idatt2506.stud.williagt.register_friend

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.ActivityCompat.startForegroundService
import androidx.core.content.ContextCompat

//TODO general error handling and checking user input

class MainActivity : AppCompatActivity() {
    private var friends: ArrayList<Person> = arrayListOf()
    private lateinit var adapter: ArrayAdapter<Person>
    private val TAG = "MAIN"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFriends()
        initList()
    }

    //Just putting some friends in register not as to feel lonely
    private fun initFriends() {
        friends.add(Person("Knut", "13/05/1999"))
        friends.add(Person("Arne", "14/05/1999"))
        friends.add(Person("Lena", "15/05/1999"))
    }

    private fun initList() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, friends)
        val friendsView = findViewById<ListView>(R.id.friend_list)

        friendsView.adapter = adapter
        friendsView.setOnItemClickListener { parent, view, pos, id -> //Clicking on a friend directs you to the edit page
            val intent = Intent(this, EditFriend::class.java)
            intent.putExtra("position", pos)
            intent.putExtra("person", friends[pos])
            startEditActivityForResult.launch(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        //Adding onclick for menu element, launching an intent for adding friend
        menu.findItem(R.id.menu_add_friend).setOnMenuItemClickListener { item: MenuItem ->
            startRegisterActivityForResult.launch(Intent(this, AddFriend::class.java)) //Clicking on add in menu directs you to add page
            return@setOnMenuItemClickListener true
        }
        return super.onCreateOptionsMenu(menu)
    }

    //Registering an activity for adding a friend
    private val startRegisterActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data!!
            val person = intent.getSerializableExtra("registeredPerson") as Person
            adapter.add(person)
        }
    }

    //Registering an activity for editing a friend
    private val startEditActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data!!
            val editedPerson = intent.getSerializableExtra("editedPerson") as Person
            val position = intent.getIntExtra("position", -1)

            friends.removeAt(position)
            friends.add(position, editedPerson)
            adapter.notifyDataSetChanged()
        }
    }
}