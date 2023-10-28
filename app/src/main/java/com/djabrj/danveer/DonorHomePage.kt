package com.djabrj.danveer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class DonorHomePage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_home_page)

        val editInfoButton = findViewById<Button>(R.id.editInfoButton)
        val donateFoodButton = findViewById<Button>(R.id.donateFoodButton)
        val viewDonationsButton = findViewById<Button>(R.id.viewDonationsButton)
        val logOutButton = findViewById<Button>(R.id.logOutButton)

        editInfoButton.setOnClickListener {
            // Handle "Edit Info" button click event
            // You can navigate to the Edit Info activity or perform other actions here.
            val intent = Intent(this, DonorProfile::class.java)
            startActivity(intent)
        }

        donateFoodButton.setOnClickListener {
            // Handle "Donate Food" button click event
            // You can navigate to the Donate Food activity or perform other actions here.
            val intent = Intent(this, DonateFood::class.java)
            startActivity(intent)
        }

        viewDonationsButton.setOnClickListener {
            // Handle "Donate Food" button click event
            // You can navigate to the Donate Food activity or perform other actions here.
            val intent = Intent(this, DonationList::class.java)
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
