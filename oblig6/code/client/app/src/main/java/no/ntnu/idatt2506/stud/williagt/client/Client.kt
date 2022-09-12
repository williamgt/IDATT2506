package no.ntnu.idatt2506.stud.williagt.client

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class Client(
    private val receiver: TextView, private val sender: TextView,
    private val SERVER_IP: String = "10.0.2.2",
    private val SERVER_PORT: Int = 12345,
) {

    private val TAG = "CLIENT"
    private lateinit var socketConnection: Socket

    private fun addMsgTextView(textView: TextView, msg: String) {
        MainScope().launch { textView.append("$msg\n") }
    }

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.i(TAG, "Connecting to server...")
            try {
                Socket(SERVER_IP, SERVER_PORT).use { socket: Socket ->
                    Log.i(TAG, "Connected to server: $socket")
                    socketConnection = socket

                    readFromServer(socket)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.i(TAG, "Caught exception: ${e.message}")
            }
        }
    }

    private fun readFromServer(socket: Socket) {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        while (true) {
            val message = reader.readLine()
            Log.i(TAG, "Got message: '$message'")
            if(message == null) break
            addMsgTextView(receiver, message)
        }
    }

    private fun sendToServer(socket: Socket, message: String) {
        //val writer = PrintWriter(socket.getOutputStream(), true)
        PrintWriter(socket.getOutputStream(), true).println(message)
        addMsgTextView(sender, message)
    }

    fun sendMsg(msg: String) {
        CoroutineScope(Dispatchers.IO).launch {
            sendToServer(socketConnection, msg)
        }
    }
}
