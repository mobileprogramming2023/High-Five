package com.seoultech.mobileprogramming.high_five.fragments

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.seoultech.mobileprogramming.high_five.DTO.Post
import com.seoultech.mobileprogramming.high_five.R
import com.seoultech.mobileprogramming.high_five.databinding.FragmentHomeBinding
import com.seoultech.mobileprogramming.high_five.databinding.FriendViewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentHomeBinding

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userId = currentUser?.uid.toString()

    val database = Firebase.database(com.seoultech.mobileprogramming.high_five.BuildConfig.FIREBASE_DATABASE_URL)
    val postDB = database.getReference("post")

    var postList = mutableListOf<Post>()

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val postAdapter = PostAdapter(postList)
        binding.postRecyclerView.adapter = postAdapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        val phOffsetItemDecoration =  PhOffsetItemDecoration(30)
        binding.postRecyclerView.addItemDecoration(phOffsetItemDecoration)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                postList.clear()
                for (postDataSnapshot in dataSnapshot.children) {
                    val postId = postDataSnapshot.value
                    val postContents: String = postDataSnapshot.child("contents").value as String
                    val postFriendUserId: String = postDataSnapshot.child("friendUserId").value as String
                    val postFriendName: String = postDataSnapshot.child("friendName").value as String
                    val postLatitude: Double = postDataSnapshot.child("latitude").value as Double
                    val postLongitude: Double = postDataSnapshot.child("longitude").value as Double
                    val postImage: String = postDataSnapshot.child("imageDownloadUri").value as String
                    val postTimestamp: Long = postDataSnapshot.child("timestamp").value as Long
                    val post = Post(contents = postContents,
                        friendUserId = postFriendUserId,
                        friendName = postFriendName,
                        latitude = postLatitude,
                        longitude = postLongitude,
                        imageDownloadUri = postImage,
                        timestamp = postTimestamp)
                    postList.add(post)
                    postAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w( "loadPost:onCancelled", databaseError.toException())
            }
        }
        postDB.child(userId).addListenerForSingleValueEvent(postListener)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class PostAdapter(val postList: MutableList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(val binding: FriendViewBinding, val context: Context) : RecyclerView.ViewHolder(binding.root) {
        private val database = Firebase.database(com.seoultech.mobileprogramming.high_five.BuildConfig.FIREBASE_DATABASE_URL)

        fun bind(post: Post) {
//            database.getReference("user").child(post.friendUserId).get().addOnSuccessListener {
//                binding.tvFriendName.text = it.child("userName").value.toString()
//            }.addOnFailureListener{
//                Log.e("firebase", "Error getting data", it)
//            }
            val locale = ConfigurationCompat.getLocales(Resources.getSystem().configuration)[0]
            binding.tvPostContents.text = post.contents
            binding.tvFriendName.text = post.friendName
            val date = Date(post.timestamp)
            val dateFormat = SimpleDateFormat("MM-dd E kk:mm", Locale(locale?.language, locale?.country))
            val strDate = dateFormat.format(date)
            binding.tvPostDatetime.text = strDate
            binding.tvPostLocation.text = getCurrentAddress(post.latitude, post.longitude)
        }

        fun getCurrentAddress(latitude: Double, longitude: Double): String {
            val geocoder = Geocoder(this.context, Locale.getDefault())
            val addressList: List<Address>? = geocoder.getFromLocation(latitude, longitude, 10)

            if (addressList == null || addressList.size == 0) {
                Toast.makeText(this.context, context.getString(R.string.address_null), Toast.LENGTH_SHORT).show()
                return context.getString(R.string.address_null)
            }
            else {
                val address: Address = addressList[0]
                return address.getAddressLine(0).toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val friendViewBinding = FriendViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(friendViewBinding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = postList.get(position)
        holder.bind(post)
        holder.apply {
            Glide.with(context).load(post.imageDownloadUri).override(500).into(binding.imageView)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }
}

class PhOffsetItemDecoration(val padding: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = padding
        outRect.bottom = padding
        outRect.left = padding
        outRect.right = padding

    }
}
