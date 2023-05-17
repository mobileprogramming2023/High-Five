package com.seoultech.mobileprogramming.high_five

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.seoultech.mobileprogramming.high_five.databinding.ActivityLoginBinding
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.fragments.PostFragment

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("highfive", "mainactivity created")
        val userName = getUserInfo()

        var postFragment = PostFragment.newInstance("hello, ", userName!!)
        val fragmentManager = supportFragmentManager
        fragmentManager.commit {
            add(binding.fragmentContainerView.id, postFragment)
        }

    }

    fun getUserInfo(): String? {
        val intent = getIntent()
        Log.d("highfive", intent.toString())

        val name: String? = intent.getStringExtra("name")
        val photoUrl: String? = intent.getStringExtra("photoUrl")

        binding.tvName.text = name
        Glide.with(this).load(photoUrl).into(binding.ivProfile)

        return name
    }

}