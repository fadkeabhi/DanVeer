package com.djabrj.danveer

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUp : AppCompatActivity() {


    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        signUpButton = findViewById(R.id.signUpButton)
        firebaseAuth = FirebaseAuth.getInstance()

        var email: String = ""
        var password: String = ""

        signUpButton.setOnClickListener {
            email = emailEditText.text.toString()
            password = passwordEditText.text.toString()

            if(!isEmailValid(email)){
                Toast.makeText(
                    this,
                    "Invalid Email.",
                    Toast.LENGTH_SHORT
                ).show()
            } else if(!isPasswordValid(password)){
                Toast.makeText(
                    this,
                    "password length At least 5.",
                    Toast.LENGTH_SHORT
                ).show()
            } else{
//                Sign up here
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            //Add details in users collection
                            val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

                            val db = FirebaseFirestore.getInstance()
                            val collectionName = "users"

                            val data = hashMapOf(
                                "email" to email,
                                "role" to ""
                            )

                            db.collection(collectionName)
                                .add(data)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {


                                        // Sign up was successful, navigate to the LoginActivity
                                        Toast.makeText(
                                            this,
                                            "Signup successfully. Contact admin for verification.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(this, LoginActivity::class.java)
                                        startActivity(intent)


                                    } else {

                                        Toast.makeText(
                                            this,
                                            "Signup failed./db error",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                }




                        } else {
                            // Login failed, show an error message
                            Toast.makeText(
                                this,
                                "Signup failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

        }
    }

    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 5
    }
}