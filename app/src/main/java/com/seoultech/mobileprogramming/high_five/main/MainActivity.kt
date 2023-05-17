package com.seoultech.mobileprogramming.high_five

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.databinding.FragmentMainBinding
import com.seoultech.mobileprogramming.high_five.ui.dashboard.DashboardFragment
import com.seoultech.mobileprogramming.high_five.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // View Binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val bindingFragment = FragmentMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom Navigation View
        if (savedInstanceState == null) {
            val bundle = bundleOf("some_int" to 0)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MainFragment>(R.id.action_mainFragment_to_mainActivity, args = bundle)
            }

            val intent = intent
            Log.d("highfive", intent.toString())

            val name: String? = intent.getStringExtra("name")
            val photoUrl: String? = intent.getStringExtra("photoUrl")
            bindingFragment.userName.text = name
            Glide.with(this).load(photoUrl).into(bindingFragment.userProfileImage)

            Log.d("highfive", name.toString())
            Log.d("highfive", photoUrl.toString())
        }

        // Replace to each fragments
        binding.BottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_main -> {
                    replaceFragment(MainFragment())
                    Log.d(TAG, "Replace to MainFragment")
                    true
                }

                R.id.navigation_dashboard -> {
                    replaceFragment(DashboardFragment())
                    Log.d(TAG, "Replace to DashboardFragment")
                    true
                }

                R.id.navigation_dm -> {
                    replaceFragment(DashboardFragment())
                    Log.d(TAG, "Replace to DMFragment")
                    true
                }

                R.id.navigation_settings -> {
                    replaceFragment(SettingsFragment())
                    Log.d(TAG, "Replace to SettingsFragment")
                    true
                }

                else -> false
            }
        }

    }

    // Set the profile of user


}

private fun replaceFragment(fragment: Fragment) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(R.id.navigation_main, fragment)
    fragmentTransaction.commit()
}

