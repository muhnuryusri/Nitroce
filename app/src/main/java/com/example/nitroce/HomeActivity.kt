package com.example.nitroce

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.nitroce.databinding.ActivityHomeBinding
import com.example.nitroce.ui.AlbumActivity
import com.example.nitroce.ui.WifiActivity

class HomeActivity : AppCompatActivity() {

private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnUpload.setOnClickListener {
            intent = Intent(this, WifiActivity::class.java)
            startActivity(intent)
        }

        binding.btnTake.setOnClickListener {
            intent = Intent(this, WifiActivity::class.java)
            startActivity(intent)
        }

        binding.btnUpload.setOnClickListener {
            intent = Intent(this, AlbumActivity::class.java)
            startActivity(intent)
        }
    }
}