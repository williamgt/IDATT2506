package no.ntnu.idatt2506.stud.williagt.server

import android.util.Log
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket

class Server(private val receiver: TextView, private val sender: TextView, private val PORT: Int = 12345) {
    private val TAG = "SERVER"
    private val socketConnections: ArrayList<Socket> = ArrayList()

    private fun addMsgTextView(textView: TextView, msg: String) {
        MainScope().launch { textView.append("$msg\n") }
    }

    fun start() {
        CoroutineScope(Dispatchers.IO).launch {

            try {
                Log.i(TAG, "Starting server....")
                ServerSocket(PORT).use { serverSocket: ServerSocket ->

                    Log.i(TAG, "ServerSocket created")
                    while (true) {
                        Log.i(TAG, "Waiting for client to connect...")
                        val socket = serverSocket.accept()
                        CoroutineScope(Dispatchers.IO).launch {

                            socket.use { clientSocket: Socket ->
                                Log.i(TAG, "A client connected  $clientSocket")
                                socketConnections.add(clientSocket) //Does not take concurrency into consideration

                                readFromClient(clientSocket)
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Log.i(TAG, "Caught exception: ${e.message}")
            }
        }
    }

    //Reads msg from client, broadcasts to every connection to the server
    private fun readFromClient(socket: Socket) {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        while (true) {
            val message = reader.readLine()
            Log.i(TAG, "Got message: '$message'")
            if(message == null) {
                socketConnections.remove(socket) //Socket is no longer connected, removing from list. Does not take concurrency into consideration
                break
            }
            addMsgTextView(receiver, message)
            for (c in socketConnections) { //Broadcasts to every connection
                if(c == socket) continue //Don't need to echo message, skipping
                sendToClient(c, message)
            }
        }
    }

    private fun sendToClient(socket: Socket, message: String) {
        PrintWriter(socket.getOutputStream(), true).println(message)
        addMsgTextView(sender, message)
    }

    fun sendMsg(msg: String) {
        CoroutineScope(Dispatchers.IO).launch {
            for (c in socketConnections) { //Broadcasts every msg from server to every connection
                sendToClient(c, msg)
            }
        }
    }
}
