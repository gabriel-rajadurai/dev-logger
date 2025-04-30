package com.gabriel.loggersample

import android.os.Build
import android.os.Bundle
import android.os.Process
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.gabriel.devlogger.DevLog
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnConnect).setOnClickListener {
            initializeDevLog()
        }

        val etTag = findViewById<TextInputEditText>(R.id.etTag)
        etTag.setText(TAG)

        findViewById<Button>(R.id.btnLog).setOnClickListener {
            val tag = etTag.text.toString()
            DevLog.v(tag, "Verbose log")
            DevLog.d(tag, "Debug log")
            DevLog.e(tag, "Error log")
            DevLog.w(tag, "Warning log")
            DevLog.i(tag, "Info log")
        }
    }

    private fun initializeDevLog() {
        val etUrl = findViewById<TextInputEditText>(R.id.etUrl)
        val hostUrl = etUrl.text.toString()
        val id = "${Build.MANUFACTURER}-${Build.MODEL}"
        DevLog.init(
            id,
            "$packageName(${Process.myPid()})",
            hostUrl,
            8080,
            "/log"
        )
        findViewById<Button>(R.id.btnConnect).isEnabled = false
        findViewById<Button>(R.id.btnLog).isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()
        DevLog.stopLogging()
    }
}