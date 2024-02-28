package com.example.anonifydemo.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding :FragmentProfileBinding?=null
    private val binding get() = _binding
    //private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

}