package com.example.anonifydemo.ui.Settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentFragmentSettingsBinding

class Fragment_Settings : Fragment() {

    private var _binding :FragmentFragmentSettingsBinding?=null
    private val binding get() = _binding

//    private val viewModel: FragmentSettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentFragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }
}