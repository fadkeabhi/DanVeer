package com.djabrj.danveer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class NgoHomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_home_page)


        val editInfoButton = findViewById<Button>(R.id.editInfoButton)
        val searchDonationButton = findViewById<Button>(R.id.searchDonations)
        val logOutButton = findViewById<Button>(R.id.logOutButton)
        val activeDonations = findViewById<Button>(R.id.activeDonations)


        editInfoButton.setOnClickListener {
            val intent = Intent(this, NgoProfile::class.java)
            startActivity(intent)
        }

        searchDonationButton.setOnClickListener {
            val intent = Intent(this, NgoFindDonations::class.java)
            startActivity(intent)
        }

        activeDonations.setOnClickListener {
            val intent = Intent(this, NgoActiveDonations::class.java)
            startActivity(intent)
        }

        logOutButton.setOnClickListener {
            // Handle "Donate Food" button click event
            // You can navigate to the Donate Food activity or perform other actions here.

            val firebaseAuth = FirebaseAuth.getInstance()
            firebaseAuth.signOut()

            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }

    }
}