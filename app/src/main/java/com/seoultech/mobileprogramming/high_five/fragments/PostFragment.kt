package com.seoultech.mobileprogramming.high_five.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.seoultech.mobileprogramming.high_five.BuildConfig
import com.seoultech.mobileprogramming.high_five.DTO.Post
import com.seoultech.mobileprogramming.high_five.MainActivity
import com.seoultech.mobileprogramming.high_five.QRCodeScan
import com.seoultech.mobileprogramming.high_five.R as hfRes
import com.seoultech.mobileprogramming.high_five.databinding.FragmentPostBinding
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PostFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PostFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentPostBinding

    var PICK_IMAGE_FROM_ALBUM = 0
    var photoUri: Uri? = null

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid.toString()
    val userName = currentUser?.displayName

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var location: Location
    lateinit var locationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    val database =
        Firebase.database(com.seoultech.mobileprogramming.high_five.BuildConfig.FIREBASE_DATABASE_URL)
    val databaseReference = database.getReference("post")
    val storage = FirebaseStorage.getInstance(BuildConfig.FIREBASE_STORAGE_URL)
    val storageReference = storage.getReference()
    var userStorageReference = storageReference.child(userId)

    var friendUid: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            photoUri = data?.data
            binding.uploadImage.setImageURI(photoUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)

        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (checkPermissionForLocation(this.requireContext())) {
            startLocationUpdates()
        }

        binding.btnInsertImg.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }

        binding.btnSave.setOnClickListener {
            if ((binding.tvInput.text?.length ?: 0) == 0) {
                Toast.makeText(this.requireContext(), hfRes.string.post_failed_no_text, Toast.LENGTH_SHORT).show()
            } else if (friendUid == null) {
                Toast.makeText(this.requireContext(), hfRes.string.post_failed_no_qr, Toast.LENGTH_SHORT).show()
            } else if (photoUri == null) {
                addPost("", binding.tvInput.text.toString())
            } else {
                val imageStorageReference =
                    userStorageReference.child("${userId}/post/images/${photoUri!!.lastPathSegment}")
                var uploadTask = imageStorageReference.putFile(photoUri!!)
                lateinit var downloadUri: Uri

                uploadTask.addOnFailureListener {
                    TODO("Handle uncessessful uploads")
                }.addOnSuccessListener { taskSnapshot ->
                }

                val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageStorageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        downloadUri = task.result
                        addPost(downloadUri.toString(), binding.tvInput.text.toString())
                    } else {
                        TODO("Handle failures")
                    }
                }
            }
            Toast.makeText(
                this.requireContext(),
                getString(hfRes.string.post_success),
                Toast.LENGTH_SHORT
            ).show()
        }

        var qrCodeScan = QRCodeScan(this)
        binding.btnQrScan.setOnClickListener() {
            qrCodeScan.startQRScan()
        }

        return binding.root
    }


    fun addPost(imageDownloadUri: String, content: String) {
        val post: Post = Post(
            imageDownloadUri = imageDownloadUri,
            contents = content,
            friendUserId = friendUid!!,
            friendName = friendName!!,
            timestamp = System.currentTimeMillis(),
            latitude = location.latitude,
            longitude = location.longitude
        )
        databaseReference.child(userId).push().setValue(post)
    }

    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    private fun startLocationUpdates() {
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this.requireActivity())
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            locationResult.lastLocation
            locationResult.lastLocation?.let { onLocationChanged(it) }
            location = locationResult.lastLocation!!
            location.latitude = locationResult.lastLocation?.latitude ?: 0.0
            location.longitude = locationResult.lastLocation?.longitude ?: 0.0
//            TODO("location이 null일 때 처리")
        }
    }

    fun onLocationChanged(location: Location) {
        val lastLocation = location
        Log.d(
            "highfive",
            "위도: ${location.latitude}, 경도: ${location.longitude}, ${lastLocation.bearing}"
        )
        val currentAddress: String = getCurrentAddress(location.latitude, location.longitude)
        binding.tvLocation.text = currentAddress
    }

    fun getCurrentAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this.requireContext(), Locale.getDefault())
        val addressList: List<Address>? = geocoder.getFromLocation(latitude, longitude, 10)

        if (addressList == null || addressList.size == 0) {
            Toast.makeText(this.requireContext(), hfRes.string.address_null, Toast.LENGTH_SHORT).show()
            return "주소 없음"
        } else {
            val address: Address = addressList[0]
            return address.getAddressLine(0).toString()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PostFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}