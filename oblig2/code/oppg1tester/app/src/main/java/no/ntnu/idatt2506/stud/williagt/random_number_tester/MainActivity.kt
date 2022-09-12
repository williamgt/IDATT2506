package no.ntnu.idatt2506.stud.williagt.random_number_tester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra("random_number", 42)
        }
        resultLauncher.launch(sendIntent)
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val num = data?.getIntExtra("random_number", 0)
            val text: TextView = findViewById<TextView>(R.id.tester_text)
            text.setText("Got random number " + num!!.toString())
        }
    }
}