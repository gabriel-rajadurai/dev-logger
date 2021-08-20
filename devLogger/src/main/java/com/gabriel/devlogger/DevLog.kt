package com.gabriel.devlogger

import android.util.Log
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.launch

object DevLog {

    private var webSocketSession: DefaultClientWebSocketSession? = null
    private const val TAG = "DevLog"

    /**
     * Initialize the logging library. This sets up the WebSocketSession
     * The session is established to host [hostUrl]/[path] at port [port]
     */
    fun init(userId: String, processName: String, hostUrl: String, port: Int, path: String = "") {
        CoroutineScope(Dispatchers.IO).launch {
            val client = HttpClient {
                install(WebSockets)
            }
            client.ws(
                host = hostUrl,
                port = port,
                path = "$path/$userId/$processName"
            ) {
                webSocketSession = this
                try {
                    while (true) {
                        incoming.receive()
                    }
                } catch (e: ClosedReceiveChannelException) {
                    webSocketSession = null
                    Log.d(TAG, "Server Disonnected")
                }
            }
        }

    }

    fun v(tag: String, message: String) {
        logMessage(LogLevel.VERBOSE, tag, message)
        Log.v(tag, message)
    }

    fun d(tag: String, message: String) {
        logMessage(LogLevel.DEBUG, tag, message)
        Log.d(tag, message)
    }

    fun e(tag: String, message: String) {
        logMessage(LogLevel.ERROR, tag, message)
        Log.e(tag, message)
    }

    fun w(tag: String, message: String) {
        logMessage(LogLevel.WARNING, tag, message)
        Log.w(tag, message)
    }

    fun i(tag: String, message: String) {
        logMessage(LogLevel.INFO, tag, message)
        Log.i(tag, message)
    }

    fun stopLogging() {
        CoroutineScope(Dispatchers.IO).launch {
            webSocketSession?.close()
        }
    }

    private fun logMessage(logLevel: LogLevel, tag: String, message: String) {

        val logTag = if (tag.length > 23) {
            Log.e(TAG, "Truncating Tag, length cannot be more than 23")
            tag.take(23)
        } else {
            tag
        }

        val logMessage = LogMessage(
            logLevel.level,
            logTag,
            System.currentTimeMillis(),
            message
        )

        val log = Gson().toJson(logMessage)
        CoroutineScope(Dispatchers.IO).launch {
            webSocketSession?.send(log)
        }
    }

    private enum class LogLevel(val level: Int) {
        VERBOSE(Log.VERBOSE),
        DEBUG(Log.DEBUG),
        INFO(Log.INFO),
        WARNING(Log.WARN),
        ERROR(Log.ERROR)
    }

    private data class LogMessage(
        val logLevel: Int,
        val tag: String,
        val timeMills: Long,
        val message: String
    )
}