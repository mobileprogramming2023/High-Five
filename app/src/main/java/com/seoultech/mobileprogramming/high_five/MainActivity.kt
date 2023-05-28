package com.seoultech.mobileprogramming.high_five

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
<<<<<<< HEAD
<<<<<<< HEAD
=======
import com.bumptech.glide.Glide
>>>>>>> 51ab0800e9b5a72a85a5b8128d3e66cfdfa41cea
=======
>>>>>>> e2ec0dcd4c2414d7ffc38fa9416b4e29c90c10ba
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.fragments.*

class MainActivity : AppCompatActivity() {

<<<<<<< HEAD
<<<<<<< HEAD
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    val fragmentManager = supportFragmentManager
=======
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
>>>>>>> 51ab0800e9b5a72a85a5b8128d3e66cfdfa41cea
=======
    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    val fragmentManager = supportFragmentManager
>>>>>>> e2ec0dcd4c2414d7ffc38fa9416b4e29c90c10ba

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> e2ec0dcd4c2414d7ffc38fa9416b4e29c90c10ba
        val homeFragment = HomeFragment()
        val postFragment = PostFragment()
        val mapsFragment = MapsFragment()
        val userProfileFragment = UserProfileFragment()
<<<<<<< HEAD
=======
        var homeFragment = HomeFragment()
        var postFragment = PostFragment()
        var mapsFragment = MapsFragment()
        var userProfileFragment = UserProfileFragment()
        var userQrFragment = UserQrFragment()
>>>>>>> 51ab0800e9b5a72a85a5b8128d3e66cfdfa41cea

        fragmentManager.commit {
            add(binding.fragmentContainerView.id, homeFragment)
<<<<<<< HEAD
=======
            add(binding.fragmentContainerView.id, userProfileFragment)
>>>>>>> 51ab0800e9b5a72a85a5b8128d3e66cfdfa41cea
=======
        val userQrFragment = UserQrFragment()

        fragmentManager.commit {
            add(binding.fragmentContainerView.id, homeFragment)
>>>>>>> e2ec0dcd4c2414d7ffc38fa9416b4e29c90c10ba
        }

        binding.navigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.homeFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, homeFragment) }
                R.id.postFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, postFragment) }
                R.id.mapFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, mapsFragment) }
                R.id.userPageFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, userProfileFragment) }
                R.id.userQRFragment -> fragmentManager.commit { replace(binding.fragmentContainerView.id, userQrFragment) }
            }
            true
        }
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
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

>>>>>>> 51ab0800e9b5a72a85a5b8128d3e66cfdfa41cea
=======
>>>>>>> e2ec0dcd4c2414d7ffc38fa9416b4e29c90c10ba
}