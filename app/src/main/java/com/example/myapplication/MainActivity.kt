package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var mapsBtn: ImageButton
    private lateinit var addBtn: ImageButton
    private lateinit var buyBtn: ImageButton
    private lateinit var callBtn: ImageButton
    private lateinit var Buy: Button



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        mapsBtn = findViewById(R.id.mapsBtn)
        addBtn = findViewById(R.id.addBtn)
        buyBtn = findViewById(R.id.buyBtn)
        callBtn = findViewById(R.id.callBtn)

        mapsBtn.setOnClickListener {
            val intent = Intent(this, mapActivity::class.java)
            startActivity(intent)
        }

        addBtn.setOnClickListener{
            val intent = Intent(this, addItemsActivity::class.java)
            startActivity(intent)
        }

        buyBtn.setOnClickListener{
            val intent = Intent(this, buyActivity::class.java)
            startActivity(intent)
        }

        callBtn.setOnClickListener{
            val intent = Intent(this, VoipActivity::class.java)
            startActivity(intent)
        }
    }
}
