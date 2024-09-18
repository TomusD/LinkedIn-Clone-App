package com.example.front

import okhttp3.WebSocket
import okhttp3.WebSocketListener

// WebSocket client
class ChatWebSocketListener : WebSocketListener() {
    var onMessageReceived: ((String) -> Unit)? = null

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageReceived?.invoke(text)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

}

