package com.example.myapplication

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage



class addItemsActivity : AppCompatActivity() {



    private lateinit var addButton: Button
    private lateinit var backButton: Button
    private lateinit var addLocBtn: Button
    private lateinit var image: ImageView
    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productQuantity: EditText

    var database = FirebaseDatabase.getInstance().reference



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_items)

        addButton = findViewById(R.id.addButton)
        backButton = findViewById(R.id.backButton)
        addLocBtn = findViewById(R.id.addLocBtn)
        image = findViewById(R.id.image)
        productName = findViewById(R.id.productName)
        productPrice = findViewById(R.id.productPrice)
        productQuantity = findViewById(R.id.productQuantity)
        //selectImage = findViewById(R.id.selectImage)


        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent(), ActivityResultCallback{
            image.setImageURI(it)
        })

        image.setOnClickListener{
            pickImage.launch("image/*")
        }

            backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        addLocBtn.setOnClickListener {
            val intent = Intent(this, locationActivity::class.java)
            startActivity(intent)
        }

        addButton.setOnClickListener{

            val name = productName.text.toString()
            val price = productPrice.text.toString()
            val quantity = productQuantity.text.toString()


            if(productName.text.isEmpty() || productPrice.text.isEmpty() || productQuantity.text.isEmpty()){
                Toast.makeText(this, "Process failed", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            database.child("Drink").child(name.toString()).setValue(Product(name, price, quantity))
            Toast.makeText(this, "Process completed", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }
    }
    }
