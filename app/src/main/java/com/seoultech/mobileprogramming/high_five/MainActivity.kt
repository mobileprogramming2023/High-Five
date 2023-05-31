package com.seoultech.mobileprogramming.high_five

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.seoultech.mobileprogramming.high_five.ShakeDetector.OnShakeListener
import com.seoultech.mobileprogramming.high_five.databinding.ActivityMainBinding
import com.seoultech.mobileprogramming.high_five.fragments.*
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    private val fragmentManager = supportFragmentManager

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var shakeDetector: ShakeDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        shakeDetector = ShakeDetector()
        Log.d("highfive", "shakeDetector: $shakeDetector")
        val context: Context = this
        shakeDetector.setOnShakeListener(object : OnShakeListener {
            override fun onShake(count: Int) {
                Log.d("highfive", "shake detected")

//                감지시 할 작업 작성
//                val intent = Intent(this, CreateQR::class.java)
                val intent: Intent = Intent(context, CreateQrActivity::class.java)
                startActivity(intent)

            }
        })

        val homeFragment = HomeFragment()
        val postFragment = PostFragment()
        val mapsFragment = MapsFragment()
        val userProfileFragment = UserProfileFragment()
        val userQrFragment = UserQrFragment()

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

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        sensorManager.unregisterListener(shakeDetector)
        super.onPause()
    }

}