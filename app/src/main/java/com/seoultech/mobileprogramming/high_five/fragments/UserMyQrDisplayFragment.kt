package com.seoultech.mobileprogramming.high_five.fragments

import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.google.firebase.auth.FirebaseAuth
import com.seoultech.mobileprogramming.high_five.R
import com.seoultech.mobileprogramming.high_five.databinding.FragmentMyQrDisplayBinding
import com.seoultech.mobileprogramming.high_five.databinding.FragmentUserQrBinding
import com.seoultech.mobileprogramming.high_five.qrCode.sendHtmlTextToWebView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserQrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserMyQrDisplayFragment : Fragment() {
    lateinit var binding: FragmentMyQrDisplayBinding


    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Get current user's uid
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid.toString()

        // Get html text from qrCode/sendHtmlTextToWebView.kt
        val htmlText = sendHtmlTextToWebView().userQrEncode(userId)
        val encodedHtml = Base64.encodeToString(htmlText.toByteArray(), Base64.NO_PADDING)

        binding = FragmentMyQrDisplayBinding.inflate(inflater, container, false)

        binding.btnScanFriend.setOnClickListener {
            View.inflate(
                context,
                R.layout.qr_scan_others_view,
                null
            )
        }


        // Renders QR code through the webView, implemented in html file
        var webViewQrBinding: WebView = binding.wvQrGenerated
        webViewQrBinding.settings.javaScriptEnabled = true
        webViewQrBinding.loadData(
            encodedHtml, "text/html", "base64"
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserQrFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) = UserQrFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }


}