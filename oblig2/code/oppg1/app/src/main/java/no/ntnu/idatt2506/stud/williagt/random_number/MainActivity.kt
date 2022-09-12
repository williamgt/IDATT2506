package no.ntnu.idatt2506.stud.williagt.random_number

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //task 1 a
        /*val toast = Toast.makeText(applicationContext, ranInt(100).toString(), Toast.LENGTH_LONG)
        toast.show()*/

        //This should only be used by other activities

        //Get max from intent connecting this activity and activity trying to use this one
        var max: Int = 100
        Log.i("RanNumGen", "Someone requested a random number")

        //Extracting random number
        max = intent.getIntExtra("random_number", max)
        Log.i("RanNumGen", "Requested num to be between 0 and " + max)

        //Get random number based on request
        val ranNum = ranInt(max)
        setResult(RESULT_OK, Intent().putExtra("random_number", ranNum))
        Log.i("RanNumGen", "Sending back " + ranNum)


        //Finishing
        Log.i("RanNumGen", "Finishing...")
        finish()
    }

    fun ranInt(max: Int): Int {
        return (0..max).random()
    }
}