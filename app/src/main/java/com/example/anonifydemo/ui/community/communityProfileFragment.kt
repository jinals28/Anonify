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
import com.example.anonifydemo.databinding.FragmentCommunityProfileBinding
import com.example.anonifydemo.databinding.FragmentSignInBinding
import com.example.anonifydemo.ui.signin.SignInFragmentDirections
import com.google.android.gms.common.SignInButton

class communityProfileFragment : Fragment() {

    private var _binding : FragmentCommunityProfileBinding? = null

    private val binding get() = _binding
    private lateinit var btnback : ImageButton
    //private lateinit var viewModel: CommunityProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommunityProfileBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnback = binding!!.btnback
        btnback.setOnClickListener {
            if (findNavController().currentDestination!!.id == R.id.communityProfileFragment){
                val action = communityProfileFragmentDirections.actionCommunityProfileFragmentToSearchCommunityFragment()
                findNavController().navigate(action)
            }
        }
    }

}