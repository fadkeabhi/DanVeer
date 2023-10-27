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


        editInfoButton.setOnClickListener {
            // Handle "Edit Info" button click event
            // You can navigate to the Edit Info activity or perform other actions here.
            val intent = Intent(this, NgoProfile::class.java)
            startActivity(intent)
        }

    }
}