package com.seoultech.mobileprogramming.high_five.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoultech.mobileprogramming.high_five.DTO.Post
import com.seoultech.mobileprogramming.high_five.R

class MapsFragment : Fragment() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid.toString()

    val database = Firebase.database(com.seoultech.mobileprogramming.high_five.BuildConfig.FIREBASE_DATABASE_URL)
    val postDB = database.getReference("post")

    var postList = mutableListOf<Post>()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        database.getReference("post").child(userId).get().addOnSuccessListener {
            var postLatitude: Double = 0.0;
            var postLongitude: Double = 0.0;
            for (postDataSnapshot in it.children) {
                postLatitude = postDataSnapshot.child("latitude").value as Double
                postLongitude = postDataSnapshot.child("longitude").value as Double
                val postContent = postDataSnapshot.child("contents").value as String
                val postFriendUid = postDataSnapshot.child("friendUserId").value as String
                val postFriendName = postDataSnapshot.child("friendName").value as String
                val markerOptions = MarkerOptions()
                markerOptions
                    .position(LatLng(postLatitude, postLongitude))
                    .title(postContent)
                    .snippet("with $postFriendName")
                googleMap.addMarker(markerOptions)
            }
            googleMap.moveCamera(
                CameraUpdateFactory
                    .newLatLngZoom(LatLng(postLatitude, postLongitude), 13F)
            )
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}