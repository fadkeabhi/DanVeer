package com.djabrj.danveer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firebaseAuth = FirebaseAuth.getInstance()

        // Check if a user is already logged in
        val currentUser = firebaseAuth.currentUser
        if(currentUser != null){
            val email = firebaseAuth.currentUser?.email.toString()
            getUser(email)
        }
        else{
            goToLogin()
        }
    }


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
//                            val intent = Intent(this, DonorHomePage::class.java)
//                            finish()
//                            startActivity(intent)
                            goToHomePage("donor")
                        }

                        "ngo" -> {
//                            val intent = Intent(this, NgoHomePage::class.java)
//                            finish()
//                            startActivity(intent)
                            goToHomePage("ngo")
                        }
                        else -> {
                            goToLogin()
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors (e.g., network issues, permission denied)
                goToLogin()
            }

    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)
    }

    private fun goToHomePage(role: String){
        // Delayed intent to the new activity after 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if(role == "donor"){
                Intent(this, DonorHomePage::class.java)
            } else{
                Intent(this, NgoHomePage::class.java)
            }
            startActivity(intent)
            finish() // Optional: Close the current activity if needed
        }, 0) // 5000 milliseconds (5 seconds)
    }

}