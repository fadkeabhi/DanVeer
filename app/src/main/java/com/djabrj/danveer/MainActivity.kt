package com.djabrj.danveer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, LoginActivity::class.java)
        finish()
        startActivity(intent)

    }

    override fun onRestart() {
        super.onRestart()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}