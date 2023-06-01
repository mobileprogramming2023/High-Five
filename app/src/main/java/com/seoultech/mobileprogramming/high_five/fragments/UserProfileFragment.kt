package com.seoultech.mobileprogramming.high_five.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.seoultech.mobileprogramming.high_five.LoginActivity
import com.seoultech.mobileprogramming.high_five.MainActivity
import com.seoultech.mobileprogramming.high_five.R
import com.seoultech.mobileprogramming.high_five.databinding.FragmentUserProfileBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER_NAME = "userName"
private const val USER_PHOTO_URL = "userPhotoUrl"

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var binding: FragmentUserProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private var userName = currentUser?.displayName
    private var userPhotoUrl = currentUser?.photoUrl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        binding.tvName.text = userName
        Glide.with(this).load(userPhotoUrl).into(binding.ivProfile)

        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix =
                multiFormatWriter.encode(currentUser!!.uid, BarcodeFormat.QR_CODE, 400, 400)
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.createBitmap(bitMatrix)
            binding.qrcodeProfilePage.setImageBitmap(bitmap)
        } catch (e: Exception) {
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogout.setOnClickListener() {
            googleLogout()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userName Parameter 1.
         * @param userPhotoUrl Parameter 2.
         * @return A new instance of fragment UserProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userName: String?, userPhotoUrl: String?) =
            UserProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_NAME, userName)
                    putString(USER_PHOTO_URL, userPhotoUrl)
                }
            }
    }

    private fun googleLogout() {
        val googleSignInOptions: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val googleSignInClient =
            this.let { GoogleSignIn.getClient(it.requireActivity(), googleSignInOptions) }

        auth.signOut()
        googleSignInClient.signOut()

        Toast.makeText(
            this.requireContext(),
            this.getString(R.string.logout_successed),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this.requireContext(), LoginActivity::class.java)
        intent.putExtra("logout", true)
        startActivity(intent)
    }

}