package com.example.nitroce.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nitroce.LiveViewActivity
import com.example.nitroce.databinding.ActivityWifiBinding

class WifiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWifiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnContinue.setOnClickListener {
            intent = Intent(this, LiveViewActivity::class.java)
            startActivity(intent)
        }
    }
}