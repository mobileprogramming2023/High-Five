package com.seoultech.mobileprogramming.high_five.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.seoultech.mobileprogramming.high_five.R
import com.seoultech.mobileprogramming.high_five.databinding.FragmentUserQrBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserQrFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserQrFragment : Fragment(R.layout.fragment_user_qr) {
    private lateinit var binding: FragmentUserQrBinding
    private lateinit var qrCollectionAdapter: UserQrFragmentAdapter
    private lateinit var viewPager: ViewPager2
    private val fragmentManager = childFragmentManager


//    // TODO: Rename and change types of parameters

    //    private var param1: String? = null
//    private var param2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val friendQrScanFragment = FriendQrScanFragment()
        val userMyQrDisplayFragment = UserMyQrDisplayFragment()

        fragmentManager.commit {
            setReorderingAllowed(true)

        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Declare ViewBinding
        binding = FragmentUserQrBinding.inflate(inflater, container, false)


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = binding.viewPager

        // Define TabLayoutMediator to control fragments nested in tab layout
        val tabLayout = binding.
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabNames = listOf("My QR", "Scan Friend")
            tab.text = tabNames[position]
        }.attach()
        super.onViewCreated(view, savedInstanceState)
        qrCollectionAdapter = UserQrFragmentAdapter(this)

    }

}

class UserQrFragmentAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = UserMyQrDisplayFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return when (position) {
            0 -> UserMyQrDisplayFragment()
            else -> FriendQrScanFragment()
        }
    }
}

private const val ARG_OBJECT = "object"

