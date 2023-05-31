package com.seoultech.mobileprogramming.high_five

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.seoultech.mobileprogramming.high_five.fragments.PostFragment

class QRCodeScan(private val fragment:PostFragment) {

    /** QRCode Scan */
    fun startQRScan(){
        val intentIntegrator = IntentIntegrator.forSupportFragment(fragment)

        intentIntegrator.setPrompt("안내선 안에 QR코드를 맞추면 자동으로 인식됩니다.")
        intentIntegrator.setOrientationLocked(true)
        intentIntegrator.setBeepEnabled(false)
        activityResult.launch(intentIntegrator.createScanIntent())
    }

    private val activityResult = fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        val data = result.data

        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(result.resultCode, data)
        if(intentResult != null){
            if(intentResult.contents != null){
                Toast.makeText(fragment.requireContext(), "인식된 QR-data: ${intentResult.contents}", Toast.LENGTH_SHORT).show()
                fragment.friendUid = intentResult.contents
                fragment.binding.tvFriendUid.text = intentResult.contents
            }else{
                Toast.makeText(fragment.requireContext(), "인식된 QR-data가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(fragment.requireContext(), "QR스캔에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    }
}