package com.seoultech.mobileprogramming.high_five

import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.CaptureActivity
import com.seoultech.mobileprogramming.high_five.fragments.PostFragment

class QRCodeScan(private val fragment:PostFragment) {

    val database = Firebase.database(com.seoultech.mobileprogramming.high_five.BuildConfig.FIREBASE_DATABASE_URL)

    /** QRCode Scan */
    fun startQRScan(){
        val intentIntegrator = IntentIntegrator.forSupportFragment(fragment)

        intentIntegrator.setPrompt("안내선 안에 QR코드를 맞추면 자동으로 인식됩니다.")
        intentIntegrator.setOrientationLocked(true)
        intentIntegrator.setBeepEnabled(false)
        intentIntegrator.captureActivity = CaptureActivity::class.java
        activityResult.launch(intentIntegrator.createScanIntent())
    }

    private val activityResult = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val data = result.data

        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(result.resultCode, data)
        if(intentResult != null){
            if(intentResult.contents != null){
                Toast.makeText(fragment.requireContext(), "인식된 QR-data: ${intentResult.contents}", Toast.LENGTH_SHORT).show()
                fragment.friendUid = intentResult.contents
                val userDbListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userName = snapshot.child(intentResult.contents).child("userName").value as String
                        fragment.binding.tvFriendUsername.text = userName
                        fragment.friendName = userName
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("highfive", "userDbListener failed")
                    }
                }
                database.getReference("user").addValueEventListener(userDbListener)
            }else{
                Toast.makeText(fragment.requireContext(), "인식된 QR-data가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(fragment.requireContext(), "QR스캔에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    }
}