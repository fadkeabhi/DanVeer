package com.djabrj.danveer

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class NgoHomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ngo_home_page)


        val editInfoButton = findViewById<Button>(R.id.editInfoButton)
        val searchDonationButton = findViewById<Button>(R.id.searchDonations)


        editInfoButton.setOnClickListener {
            val intent = Intent(this, NgoProfile::class.java)
            startActivity(intent)
        }

        searchDonationButton.setOnClickListener {
            val intent = Intent(this, NgoFindDonations::class.java)
            startActivity(intent)
        }

    }
}