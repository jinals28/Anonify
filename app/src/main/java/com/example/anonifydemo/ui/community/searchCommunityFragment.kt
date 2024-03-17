package com.example.anonifydemo.ui.community

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentHomeBinding
import com.example.anonifydemo.databinding.FragmentSearchCommunityBinding
import com.example.anonifydemo.ui.home.HomeFragmentDirections
import com.example.anonifydemo.ui.profile.editProfileFragmentDirections

class searchCommunityFragment : Fragment() {

    private var _binding: FragmentSearchCommunityBinding? = null
    private val binding get() = _binding

    //    private lateinit var viewModel: SearchCommunityViewModel
private lateinit var btnback : ImageButton
    private lateinit var btnhash : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchCommunityBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnback = binding!!.btnback
        btnhash = binding!!.btnCreateHash
        btnback.setOnClickListener {
            goToHomeFragment()
        }
        btnhash.setOnClickListener {
            goToCreateHashFragment()
        }
    }
    private fun goToCreateHashFragment() {
        if (findNavController().currentDestination!!.id == R.id.searchCommunityFragment) {
            val action = searchCommunityFragmentDirections.actionSearchCommunityFragmentToCreateCommunityFragment()
            findNavController().navigate(action)
        }
    }

    private fun  goToHomeFragment() {
        if (findNavController().currentDestination!!.id == R.id.searchCommunityFragment) {
            val action = searchCommunityFragmentDirections.actionSearchCommunityFragmentToNavigationHome()
            findNavController().navigate(action)
        }
    }
}