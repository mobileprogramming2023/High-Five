package com.seoultech.mobileprogramming.high_five

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.fragments.HomeFragment
import com.seoultech.mobileprogramming.high_five.fragments.MapsFragment
import com.seoultech.mobileprogramming.high_five.fragments.PostFragment
import com.seoultech.mobileprogramming.high_five.fragments.UserProfileFragment
import com.seoultech.mobileprogramming.high_five.fragments.UserQrFragment

class MainActivity : AppCompatActivity() {
    // Values for Log.d
    private val activityTag = "MainActivity"
    private val fragmentTag = "Fragment"

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val userName = getUserName()
        val userPhotoUrl = getUserPhotoUrl()

        var homeFragment = HomeFragment()
        var postFragment = PostFragment()
        var mapsFragment = MapsFragment()
        var userProfileFragment = UserProfileFragment()
        var userQrFragment = UserQrFragment()

        val fragmentManager = supportFragmentManager
        fragmentManager.commit {
            add(binding.fragmentContainerView.id, homeFragment)
            add(binding.fragmentContainerView.id, userProfileFragment)
        }

        /*
        Bottom Navigation View:
        https://developer.android.com/guide/navigation/navigation-ui#bottom_navigation
        It connects to each fragments: Home, Posting, Maps, User Profile, User QR Code
        */
        binding.navigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id, homeFragment
                    )
                }

                R.id.postFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id, postFragment
                    )
                }

                R.id.mapFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id, mapsFragment
                    )
                }

                R.id.userPageFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id, userProfileFragment
                    )
                }

                R.id.nav_user_qr_fragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id, userQrFragment
                    )
                }
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

    // https://stackoverflow.com/questions/5448653/how-to-implement-onbackpressed-in-fragments
    // Set Fragment: Declare each fragment and set the fragment when the user clicks the bottom navigation view
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
        val userQrFragment = fragmentManager.findFragmentByTag("userQr_fragment")

        // Hide fragments when accessing other fragments when escaping from current fragment
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment)
        }
        if (postFragment != null) {
            fragmentTransaction.hide(postFragment)
        }
        if (mapsFragment != null) {
            fragmentTransaction.hide(mapsFragment)
        }
        if (userProfileFragment != null) {
            fragmentTransaction.hide(userProfileFragment)
        }
        if (userQrFragment != null) {
            fragmentTransaction.hide(userQrFragment)
        }

        // Access to fragment by tag
        if (tag == "HomeFragment") {
            if (homeFragment != null) {
                fragmentTransaction.show(homeFragment)
                Log.d("$activityTag - $fragmentTag", "HomeFragment is shown")
            }
        } else if (tag == "PostFragment") {
            if (postFragment != null) {
                fragmentTransaction.show(postFragment)
                Log.d("$activityTag - $fragmentTag", "PostFragment is shown")
            }
        } else if (tag == "MapFragment") {
            if (mapsFragment != null) {
                fragmentTransaction.show(mapsFragment)
                Log.d("$activityTag - $fragmentTag", "MapFragment is shown")
            }
        } else if (tag == "UserProfileFragment") {
            if (userProfileFragment != null) {
                fragmentTransaction.show(userProfileFragment)
                Log.d("$activityTag - $fragmentTag", "UserProfileFragment is shown")
            }
        } else if (tag == "UserQrFragment") {
            if (userQrFragment != null) {
                fragmentTransaction.show(userQrFragment)
                Log.d("$activityTag - $fragmentTag", "UserQrFragment is shown")
            }
        }
        fragmentTransaction.commit()
    }

}