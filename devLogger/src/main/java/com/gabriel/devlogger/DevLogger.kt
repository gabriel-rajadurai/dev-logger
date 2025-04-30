package com.gabriel.devlogger

import android.util.Log

interface DevLogger {
    fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    fun stopLogging() {

    }
}