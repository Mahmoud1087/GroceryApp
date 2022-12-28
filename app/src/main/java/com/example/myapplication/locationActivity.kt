package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class locationActivity: AppCompatActivity() {
    private lateinit var backButton: Button
    private lateinit var addLocToDatabase: Button
    private lateinit var latEt: EditText
    private lateinit var longEt: EditText

    var database = FirebaseDatabase.getInstance().reference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_loc_activity)

        addLocToDatabase = findViewById(R.id.addLocToDatabase)
        backButton = findViewById(R.id.backButton)
        latEt = findViewById(R.id.latEt)
        longEt = findViewById(R.id.longEt)

        backButton.setOnClickListener {
            val intent = Intent(this, addItemsActivity::class.java)
            startActivity(intent)
        }

        addLocToDatabase.setOnClickListener {
            val long = longEt.text.toString()
            val lat = latEt.text.toString()

            if(longEt.text.isEmpty() || latEt.text.isEmpty()){
                Toast.makeText(this, "Process failed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            database.child("Locations").child(FirebaseAuth.getInstance().currentUser?.uid.toString()).setValue(Location(lat,long))
            Toast.makeText(this, "Process completed", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, addItemsActivity::class.java)
            startActivity(intent)
        }

    }

}
