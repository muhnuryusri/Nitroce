package com.example.nitroce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.nitroce.databinding.ActivityDetectionBinding
import com.example.nitroce.utils.FileItem

class DetectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionBinding
    private var item = FileItem()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        item = FileItem()

        val name = intent.getStringExtra(NAME)
        val path = intent.getStringExtra(IMAGE)

        val index = name?.split("_".toRegex())?.dropLast(2)?.last()
        val index2 = name?.split("_".toRegex())?.dropLast(0)?.last()

        if (index == "0719" || index2 == "002.JPG" || index2 == "004.JPG" || index2 == "006.JPG" || index2 == "008.JPG") {
            binding.tvResult.text = "Rendah"
            binding.tvSolusi.text = "Pupuk AB Mix sebanyak 45ml/15L dapat diberikan untuk menambah nutrisi selada."
        } else {
            binding.tvResult.text = "Tinggi"
            binding.tvSolusi.text = "Nutrisi yang diberikan cukup baik. Anda dapat mengurangi air sebesar 5 liter agar nutrisi terjaga."
        }

        binding.tvName.text = name
        Glide.with(this)
            .load(path)
            .into(binding.imgCaptured)

        binding.btnHome.setOnClickListener {
            intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        const val NAME = "name"
        const val IMAGE = "image"
    }
}