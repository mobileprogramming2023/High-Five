package com.seoultech.mobileprogramming.high_five

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.seoultech.mobileprogramming.high_five.databinding.ActivityLoginBinding
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()

        val name: String? = intent.getStringExtra("name")
        val photoUrl: String? = intent.getStringExtra("photoUrl")

        binding.tvName.text = name
        Glide.with(this).load(photoUrl).into(binding.ivProfile)
    }

}