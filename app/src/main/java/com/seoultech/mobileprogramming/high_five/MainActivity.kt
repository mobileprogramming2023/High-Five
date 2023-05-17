package com.seoultech.mobileprogramming.high_five

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.compose.NavHost
import com.bumptech.glide.Glide
import com.seoultech.mobileprogramming.high_five.databinding.ActivityLoginBinding
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null){
            val bundle = bundleOf("some_int" to 0)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MainFragment>(R.id.mainFragmentContainerView, args = bundle) }
        }

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        Log.d("highfive", intent.toString())

        val name: String? = intent.getStringExtra("name")
        val photoUrl: String? = intent.getStringExtra("photoUrl")

        Log.d("highfive", name.toString())
        Log.d("highfive", photoUrl.toString())

        binding.tvName.text = name
        Glide.with(this).load(photoUrl).into(binding.ivProfile)
    }

}