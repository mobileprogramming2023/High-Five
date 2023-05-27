package com.seoultech.mobileprogramming.high_five.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.seoultech.mobileprogramming.high_five.BuildConfig
import com.seoultech.mobileprogramming.high_five.DTO.Post
import com.seoultech.mobileprogramming.high_five.databinding.FragmentPostBinding

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

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val multiplePermissionsCode = 100

    val database =
        Firebase.database(com.seoultech.mobileprogramming.high_five.BuildConfig.FIREBASE_DATABASE_URL)
    val databaseReference = database.getReference(userId)
    val storage = FirebaseStorage.getInstance(BuildConfig.FIREBASE_STORAGE_URL)
    val storageReference = storage.getReference()
    var userStorageReference = storageReference.child(userId)

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

        binding.btnInsertImg.setOnClickListener {
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }

        binding.btnSave.setOnClickListener {
            if ((binding.tvInput.text?.length ?: 0) == 0) {
                Toast.makeText(this.requireContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (photoUri == null) {
                addPost("", binding.tvInput.text.toString())
            } else {
                val imageStorageReference =
                    userStorageReference.child("post/images/${photoUri!!.lastPathSegment}")
                var uploadTask = imageStorageReference.putFile(photoUri!!)
                lateinit var downloadUri: Uri

                uploadTask.addOnFailureListener {
                    TODO("Handle uncessessful uploads")
                }.addOnSuccessListener { taskSnapshot ->
                    Log.d("highfive", "$taskSnapshot")
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
                        Log.d("highfive", "$downloadUri")
                    } else {
                        TODO("Handle failures")
                    }
                }
            }
        }
        return binding.root
    }

    fun addPost(imageDownloadUri: String, content: String) {
        val testFriendUid = "freindUid_test"
        val post: Post = Post(
            imageDownloadUri,
            content,
            testFriendUid,
            System.currentTimeMillis(),
            "서울시 노원구 공릉동"
        )
        databaseReference.child("post").push().setValue(post)
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