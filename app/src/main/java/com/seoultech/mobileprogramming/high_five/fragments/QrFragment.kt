package com.seoultech.mobileprogramming.high_five.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.seoultech.mobileprogramming.high_five.R
import com.seoultech.mobileprogramming.high_five.databinding.FragmentQrBinding
import io.github.g0dkar.qrcode.ErrorCorrectionLevel
import io.github.g0dkar.qrcode.QRCode
import io.github.g0dkar.qrcode.QRCodeDataType

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [QrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QrFragment : Fragment() {
    private val tagQrCode = "QRCode"
    private val tagHighFive = "HighFive Fragment"


    private var param2: String? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        var binding = FragmentQrBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // Meeting Data : User ID, Meeting DateTime, current location (TBA)
        // Generate QR Code
        val userInfo = auth.currentUser?.uid.toString()
        // renders the QR code as a native image, by Bitmap
        val userQRCodeImage = QRCode(
            userInfo, ErrorCorrectionLevel.L, QRCodeDataType.DEFAULT
        ).render(cellSize = 30).nativeImage() as Bitmap

        Log.d("$tagHighFive: $tagQrCode", "The data is ${auth.currentUser?.uid.toString()}")

        binding.userQrRendered.setImageBitmap(userQRCodeImage)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QrCodeFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) = QrFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}