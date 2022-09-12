package no.ntnu.idatt2506.stud.williagt.guessing_game

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val wrapper = HttpWrapper("https://bigdata.idi.ntnu.no/mobil/tallspill.jsp")
    private val TAG = "MainActivity"
    private var guessedTimes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.interactButton).setOnClickListener {
            if(sendInitialRequest()) {
                updateViews()
            }
        }
    }

    private fun sendInitialRequest(): Boolean {
        val cardNumber = findViewById<TextView>(R.id.cardNumber)!!.text.toString()
        val name = findViewById<TextView>(R.id.personName)!!.text.toString()

        if(cardNumber.isEmpty() || name.isEmpty()) return false

        CoroutineScope(Dispatchers.IO).launch {
            val result = wrapper.get(mapOf("navn" to name, "kortnummer" to cardNumber)).trim() //Some problems with the string formatting, so trimming it here
            Log.i(TAG, "Got result: $result")

            MainScope().launch {
                findViewById<TextView>(R.id.feedback).text = result
            }
        }
        return true
    }

    private fun guessRequest() {
        guessedTimes++
        val guessNumber = findViewById<TextView>(R.id.guessNumber).text.toString()

        CoroutineScope(Dispatchers.IO).launch {
           val result = wrapper.get(mapOf("tall" to guessNumber)).trim()
            Log.i(TAG, "Got result: $result")

            MainScope().launch {
                findViewById<TextView>(R.id.feedback).text = result
            }
        }
        if(guessedTimes == 3 || feedbackContainsWon()) {
            guessedTimes = 0
            reset()
        }
    }

    private fun feedbackContainsWon(): Boolean {
        return findViewById<TextView>(R.id.feedback).text.toString().contains("vunnet")
    }

    private fun reset() {
        //Enabling name and card number
        val cardNumberView = findViewById<TextView>(R.id.cardNumber)!!
        cardNumberView.isFocusable = true
        cardNumberView.setTextColor(Color.BLACK)

        val nameView = findViewById<TextView>(R.id.personName)!!
        nameView.isFocusable = true
        nameView.setTextColor(Color.BLACK)

        //Making number guesser invisible and clearing the value
        val guessNumberView = findViewById<TextView>(R.id.guessNumber)
        guessNumberView.isVisible = false
        guessNumberView.text = null

        //Updating interact button
        val interactButton = findViewById<Button>(R.id.interactButton)!!
        interactButton.text = getString(R.string.restart)
        interactButton.setOnClickListener {
            if(sendInitialRequest()) {
                updateViews()
            }
        }
    }

    private fun updateViews() {
        //Disabling name and card number
        val cardNumberView = findViewById<TextView>(R.id.cardNumber)!!
        cardNumberView.isFocusable = false
        cardNumberView.setTextColor(Color.GRAY)

        val nameView = findViewById<TextView>(R.id.personName)!!
        nameView.isFocusable = false
        nameView.setTextColor(Color.GRAY)

        //Making number guesser visible
        findViewById<TextView>(R.id.guessNumber).isVisible = true

        //Updating interact button
        val interactButton = findViewById<Button>(R.id.interactButton)!!
        interactButton.text = getString(R.string.guess)
        interactButton.setOnClickListener { guessRequest() }

    }
}