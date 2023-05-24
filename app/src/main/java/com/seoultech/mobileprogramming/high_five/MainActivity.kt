package com.seoultech.mobileprogramming.high_five

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.fragments.QrFragment
import com.seoultech.mobileprogramming.high_five.fragments.HomeFragment
import com.seoultech.mobileprogramming.high_five.fragments.MapsFragment
import com.seoultech.mobileprogramming.high_five.fragments.PostFragment
import com.seoultech.mobileprogramming.high_five.fragments.UserProfileFragment

class MainActivity : AppCompatActivity() {

    // See: https://developer.android.com/training/basics/intents/result
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("highfive", "MainActivity created")
        val userName = getUserInfo()

        var homeFragment = HomeFragment()
        var postFragment = PostFragment()
        var mapsFragment = MapsFragment()
        var userProfileFragment = UserProfileFragment()
        var qrFragment = QrFragment()

        val fragmentManager = supportFragmentManager
        fragmentManager.commit {
            add(binding.fragmentContainerView.id, homeFragment)
        }

        binding.navigationView.setOnItemSelectedListener {
            Log.d("highfive", "${it.itemId}")
            when (it.itemId) {
                R.id.homeFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id,
                        homeFragment
                    )
                }

                R.id.qrFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id,
                        qrFragment
                    )
                }

                R.id.mapFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id,
                        mapsFragment
                    )
                }

                R.id.userPageFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id,
                        userProfileFragment
                    )
                }

                R.id.postFragment -> fragmentManager.commit {
                    replace(
                        binding.fragmentContainerView.id,
                        postFragment
                    )
                }
            }
            true
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

    fun getUserInfoForQR(): String? {
        val intent = intent
        Log.d("highfive", intent.toString())

        val name: String? = intent.getStringExtra("name")

        binding.tvName.text = name

        return name
    }

    // Set fragment manager for transaction
    fun setFragment(tag: String, fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(binding.fragmentContainerView.id, fragment)
        }

        // Declare fragments on navigation bar
        val homeFragment = fragmentManager.findFragmentByTag("home_fragment")
        val postFragment = fragmentManager.findFragmentByTag("post_fragment")
        val mapsFragment = fragmentManager.findFragmentByTag("maps_fragment")
        val userProfileFragment = fragmentManager.findFragmentByTag("userProfile_fragment")
        val qrFragment = fragmentManager.findFragmentByTag("qr_fragment")

        // Hide fragment
        // Home Fragment
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment)
        }
        // Post Fragment
        if (postFragment != null) {
            fragmentTransaction.hide(postFragment)
        }
        // Maps Fragment
        if (mapsFragment != null) {
            fragmentTransaction.hide(mapsFragment)
        }
        // User Profile Fragment
        if (userProfileFragment != null) {
            fragmentTransaction.hide(userProfileFragment)
        }
        // QR Fragment
        if (qrFragment != null) {
            fragmentTransaction.hide(qrFragment)
        }

        // Show fragment
        // When touch the item in Bottom Navigation View, it shows the fragment
        if (tag == "HomeFragment") {
            if (homeFragment != null) {
                fragmentTransaction.show(homeFragment)
            }
        } else if (tag == "PostFragment") {
            if (postFragment != null) {
                fragmentTransaction.show(postFragment)
            }
        } else if (tag == "MapFragment") {
            if (mapsFragment != null) {
                fragmentTransaction.show(mapsFragment)
            }
        } else if (tag == "UserProfileFragment") {
            if (userProfileFragment != null) {
                fragmentTransaction.show(userProfileFragment)
            }
        } else if (tag == "QrFragment") {
            if (qrFragment != null) {
                fragmentTransaction.show(qrFragment)
            }
        }
        fragmentTransaction.commit()
    }

}