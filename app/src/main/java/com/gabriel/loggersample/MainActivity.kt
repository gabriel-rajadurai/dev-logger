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
        val id = "${Build.MANUFACTURER}-${Build.MODEL}"
        DevLog.init(
            id,
            "$packageName(${Process.myPid()})",
            "192.168.43.230",
            8080,
            "/log"
        )

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

    override fun onDestroy() {
        super.onDestroy()
        DevLog.stopLogging()
    }
}