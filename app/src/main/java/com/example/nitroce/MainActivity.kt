package com.example.nitroce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nitroce.databinding.ActivityHomeBinding
import com.example.nitroce.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}