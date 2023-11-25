package com.djabrj.danveer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var firebaseAuth: FirebaseAuth


    private fun getUser(email:String){
        val db = FirebaseFirestore.getInstance()
        // Reference to the Firestore collection
        val usersCollection = db.collection("users")
        val query: Query = usersCollection.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Access data for each document
                    val role = document.getString("role")

                    println(role)
                    when (role) {
                        "donor" -> {
                            val intent = Intent(this, DonorHomePage::class.java)
                            finish()
                            startActivity(intent)
                        }

                        "ngo" -> {
                            val intent = Intent(this, NgoHomePage::class.java)
                            finish()
                            startActivity(intent)
                        }

                        else -> {
                            Toast.makeText(
                                this,
                                "Login failed. User not verified.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors (e.g., network issues, permission denied)
                println("Error getting documents: $exception")
                Toast.makeText(
                    this,
                    "Login failed. Please check your credentials.",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)
        firebaseAuth = FirebaseAuth.getInstance()


        // Check if a user is already logged in
        val currentUser = firebaseAuth.currentUser
        var email : String = ""
        var password : String

        if (currentUser == null) {
            // The user is not logged in, you can show the login screen or perform any other actions.

            signUpButton.setOnClickListener {
                val intent = Intent(this, SignUp::class.java)
                startActivity(intent)
            }

            loginButton.setOnClickListener {
                email = emailEditText.text.toString()
                password = passwordEditText.text.toString()

                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login was successful, navigate to the HomeActivity or your main screen
                            getUser(email)
                        } else {
                            // Login failed, show an error message
                            Toast.makeText(
                                this,
                                "Login failed. Please check your credentials.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }else{
            email = firebaseAuth.currentUser?.email.toString()
            getUser(email)
        }

//        println(email)
        // Initialize Firestore

    }


}
