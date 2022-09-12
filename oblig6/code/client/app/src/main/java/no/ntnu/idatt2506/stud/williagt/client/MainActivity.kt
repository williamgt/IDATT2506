package no.ntnu.idatt2506.stud.williagt.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sendTxtView = findViewById<TextView>(R.id.sent)
        val receiveTxtView = findViewById<TextView>(R.id.received)
        val client = Client(receiveTxtView, sendTxtView)
        client.start()
        findViewById<Button>(R.id.sendBtn).setOnClickListener {
            client.sendMsg(findViewById<TextView>(R.id.SendTxt).text.toString())
        }
    }
}