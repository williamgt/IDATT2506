package no.ntnu.idatt2506.stud.williagt.calculator_ish

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private val TAG: String = "CalcIsh"

    private lateinit var ranIntent: Intent
    private lateinit var launcher1: ActivityResultLauncher<Intent>
    private lateinit var launcher2: ActivityResultLauncher<Intent>

    private var upperLim by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        upperLim = findViewById<EditText>(R.id.upperLim).text.toString().toInt()

        //Random number intent used whenever random number is needed
        ranIntent = getRandomNumberIntent()

        //Launcher one that sets num1s value
        launcher1 = startActivityWithHandler { data ->
            findViewById<TextView>(R.id.num1).text = data?.getIntExtra("random_number", 0).toString()
        }

        //Launcher two that sets num2s value
        launcher2 = startActivityWithHandler { data ->
            findViewById<TextView>(R.id.num2).text = data?.getIntExtra("random_number", 0).toString()
        }
    }

    /**
     * Created a unified onclick method for both add and multiply button because of lots of repeated code.
     * Still have the two onclick methods separate, but will use this one in the actual application
     */
    fun onClickCalculate(v: View) {
        val num1 = findViewById<TextView>(R.id.num1).text.toString().toInt()
        val num2 = findViewById<TextView>(R.id.num2).text.toString().toInt()
        val ans = findViewById<EditText>(R.id.ans).text.toString().toInt() //TODO error checking before converting

        //Toast for when add button was clicked
        if (v.id == R.id.addButton) {
            when(num1 + num2) {
                ans -> showToastWithMsg(getString(R.string.correct))
                else -> showToastWithMsg(getString(R.string.wrong) + " " + (num1 + num2))
            }
        }

        //Toast for when multiply button was clicked
        else if (v.id == R.id.multiplyButton) {
            when(num1 * num2) {
                ans -> showToastWithMsg(getString(R.string.correct))
                else -> showToastWithMsg(getString(R.string.wrong) + " " + (num1 * num2))
            }
        }

        //Toast for when something with onclick handler other than add or multiply was clicked
        else {
            showToastWithMsg("No valid button was clicked or something idk")
        }

        setNum1AndNum2Random()
    }

    fun onClickAdd(v: View) {
        Log.i(TAG, "Add was clicked")

        val num1 = findViewById<TextView>(R.id.num1).text.toString().toInt()
        val num2 = findViewById<TextView>(R.id.num2).text.toString().toInt()
        val ans = findViewById<EditText>(R.id.ans).text.toString().toInt() //TODO error checking before converting

        when(num1 + num2) {
            ans -> showToastWithMsg(getString(R.string.correct))
            else -> showToastWithMsg(getString(R.string.wrong) + " " + (num1 + num2))
        }

        setNum1AndNum2Random()
    }

    fun onClickMultiply(v: View): Unit {
        Log.i(TAG, "Multiply was clicked")

        val num1 = findViewById<TextView>(R.id.num1).text.toString().toInt()
        val num2 = findViewById<TextView>(R.id.num2).text.toString().toInt()
        val ans = findViewById<EditText>(R.id.ans).text.toString().toInt() //TODO error checking before converting

        when(num1 * num2) {
            ans -> showToastWithMsg(getString(R.string.correct))
            else -> showToastWithMsg(getString(R.string.wrong) + " " + (num1 * num2))
        }

        setNum1AndNum2Random()
    }

    private fun showToastWithMsg(msg: String): Unit {
        val toast = Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun getRandomNumberIntent() : Intent {
        val max = findViewById<EditText>(R.id.upperLim).text.toString().toInt() //TODO error checking before converting
        val intent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra("random_number", max)
        }
        return intent
    }

    private fun startActivityWithHandler(handler: (data: Intent?) -> Unit): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                handler(data)
            }
        }
    }

    private fun setNum1AndNum2Random() {
        //Checking if upper limit is changed. If so, update upperLim and get updated random number intent
        val temp: Int = findViewById<EditText>(R.id.upperLim).text.toString().toInt() //TODO error checking before converting
        if (temp != upperLim) {
            upperLim = temp
            ranIntent = getRandomNumberIntent()
        }

        //Launching activities to set new numbers with random number intent
        launcher1.launch(ranIntent)
        launcher2.launch(ranIntent)
    }
}
