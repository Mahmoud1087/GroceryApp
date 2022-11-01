package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    lateinit var email: EditText
    lateinit var password2: EditText
    private lateinit var password: EditText
    private lateinit var signUpButton: Button
    lateinit var login: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        email = findViewById(R.id.email)
        password2 = findViewById(R.id.password2)
        password = findViewById(R.id.password1)
        signUpButton = findViewById(R.id.signUpButton)
        login = findViewById(R.id.login)

        auth = Firebase.auth

        signUpButton.setOnClickListener {
            signUp()
        }

        login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUp() {
        val email = email.text.toString()
        val pass1 = password.text.toString()
        val pass2 = password2.text.toString()

        if (email.isBlank() || pass1.isBlank() || pass2.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass1 != pass2) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT)
                .show()
            return
        }

        auth.createUserWithEmailAndPassword(email, pass1).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Email or password is incorrect. Or you already have an account", Toast.LENGTH_SHORT).show()
            }
        }
    }
}