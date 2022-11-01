package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity

class addItemsActivity : AppCompatActivity() {
    //private lateinit var addButton: Button
    private lateinit var backButton: Button
    //lateinit var itemInput: EditText
    //lateinit var listOfItems: MultiAutoCompleteTextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_items)

        //addButton = findViewById(R.id.addButton)
        backButton = findViewById(R.id.backButton)
        //itemInput = findViewById(R.id.itemInput)
        //listOfItems = findViewById(R.id.listOfItems)

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}