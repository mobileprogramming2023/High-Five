package com.seoultech.mobileprogramming.high_five

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.seoultech.mobileprogramming.high_five.databinding.ActivityLoginBinding
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.fragments.HomeFragment
import com.seoultech.mobileprogramming.high_five.fragments.MapsFragment
import com.seoultech.mobileprogramming.high_five.fragments.PostFragment
import com.seoultech.mobileprogramming.high_five.fragments.UserProfileFragment

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val userName = getUserName()
        val userPhotoUrl = getUserPhotoUrl()

        var homeFragment = HomeFragment()
        var postFragment = PostFragment()
        var mapsFragment = MapsFragment()
        var userProfileFragment = UserProfileFragment()

        val fragmentManager = supportFragmentManager
        fragmentManager.commit {
            add(binding.fragmentContainerView.id, homeFragment)
//            add(binding.fragmentContainerView.id, userProfileFragment)
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

    fun getUserName(): String? {
        val intent = getIntent()
        val name: String? = intent.getStringExtra("name")
        binding.tvName.text = name
        return name
    }

    fun getUserPhotoUrl(): String? {
        val intent = getIntent()
        val photoUrl: String? = intent.getStringExtra("photoUrl")
        Glide.with(this).load(photoUrl).into(binding.ivProfile)
        return photoUrl
    }

    fun setFragment(tag: String, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(binding.fragmentContainerView.id, fragment)
        }

        val homeFragment = fragmentManager.findFragmentByTag("home_fragment")
        val postFragment = fragmentManager.findFragmentByTag("post_fragment")
        val mapsFragment = fragmentManager.findFragmentByTag("maps_fragment")
        val userProfileFragment = fragmentManager.findFragmentByTag("userProfile_fragment")

        if (homeFragment != null) {fragmentTransaction.hide(homeFragment)}
        if (postFragment != null) {fragmentTransaction.hide(postFragment)}
        if (mapsFragment != null) {fragmentTransaction.hide(mapsFragment)}
        if (userProfileFragment != null) {fragmentTransaction.hide(userProfileFragment)}

        if (tag == "HomeFragment") {
            if (homeFragment != null) { fragmentTransaction.show(homeFragment) }
        }
        else if (tag == "PostFragment") {
            if (postFragment != null) { fragmentTransaction.show(postFragment) }
        }
        else if (tag == "MapFragment") {
            if (mapsFragment != null) { fragmentTransaction.show(mapsFragment) }
        }
        else if (tag == "UserProfileFragment") {
            if (userProfileFragment != null) { fragmentTransaction.show(userProfileFragment) }
        }
        fragmentTransaction.commit()
    }

}