package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.speech.tts.Voice
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VoipActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.voip_activity)

        findViewById<Button>(R.id.videoBtn).setOnClickListener {
            val intent = Intent(this, Video::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.voiceBtn).setOnClickListener {
            val intent = Intent(this, Voice::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.backButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}