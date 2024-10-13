package com.example.smartchess20.network

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit

class WebSocketCommunication(
    private val onMessage: (String) -> Unit,
    private val onFailure: (Throwable, Response?) -> Unit,
    private val onClosed: (Int, String) -> Unit
) {
    private lateinit var webSocket: WebSocket

    fun connectToWebSocket() {
        val client = OkHttpClient.Builder().pingInterval(10, TimeUnit.SECONDS).build()
        val request =
            Request.Builder().url("ws://192.168.4.1:81")
                .build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.v("DebugFragment", "Connection established")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                onMessage(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                onFailure(t, response)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                onClosed(code, reason)
            }
        })
    }

    fun closeWebsocket() {
        if (::webSocket.isInitialized)
            webSocket.close(1000, "Closing")
    }

    fun sendDataToServer(data: String) {
        webSocket.send(data)
    }
}