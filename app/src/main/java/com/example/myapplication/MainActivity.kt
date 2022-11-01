package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var mapsBtn: ImageButton
    private lateinit var addBtn: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        mapsBtn = findViewById(R.id.mapsBtn)
        addBtn = findViewById(R.id.addBtn)

        mapsBtn.setOnClickListener {
            val intent = Intent(this, mapActivity::class.java)
            startActivity(intent)
        }

        addBtn.setOnClickListener{
            val intent = Intent(this, addItemsActivity::class.java)
            startActivity(intent)
        }
    }
}
