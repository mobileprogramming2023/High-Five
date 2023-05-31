package com.seoultech.mobileprogramming.high_five.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.seoultech.mobileprogramming.high_five.R
import com.seoultech.mobileprogramming.high_five.databinding.FragmentFriendQrScanBinding

class FriendQrScanFragment : Fragment() {

    companion object {
        fun newInstance() = FriendQrScanFragment()
    }

    private lateinit var viewModel: FriendQrScanViewModel
    private lateinit var binding: FragmentFriendQrScanBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friend_qr_scan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FriendQrScanViewModel::class.java)
        // TODO: Use the ViewModel
    }

}