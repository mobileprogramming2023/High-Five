package com.seoultech.mobileprogramming.high_five

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.fragments.HomeFragment
import com.seoultech.mobileprogramming.high_five.fragments.MapsFragment
import com.seoultech.mobileprogramming.high_five.fragments.PostFragment
import com.seoultech.mobileprogramming.high_five.fragments.UserProfileFragment

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val postFragment = PostFragment()
        val mapsFragment = MapsFragment()
        val userProfileFragment = UserProfileFragment()

        fragmentManager.commit {
            add(binding.fragmentContainerView.id, homeFragment)
        }

        binding.navigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.homeFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, homeFragment) }
                R.id.postFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, postFragment) }
                R.id.mapFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, mapsFragment) }
                R.id.userPageFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, userProfileFragment) }
            }
            true
        }
    }

}