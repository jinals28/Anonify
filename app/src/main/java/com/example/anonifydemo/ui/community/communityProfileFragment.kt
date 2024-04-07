package com.example.anonifydemo.ui.community

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
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
    private lateinit var btnoption : ImageButton
    private lateinit var btnfollow :Button
    private lateinit var txthashtagnm : TextView
    private lateinit var txtbio : TextView
    private lateinit var txtfollowers : TextView
    private lateinit var txtpo : TextView
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
        btnoption = binding!!.btnoptions
        btnfollow = binding!!.btnFollow
        txthashtagnm = binding!!.txthashtagnm
        txtbio = binding!!.txtbio
        txtfollowers = binding!!.txtfollowers
        txtpo= binding!!.txtpo

        btnback.setOnClickListener {
            val navController = findNavController()
            if (findNavController().currentDestination!!.id == R.id.communityProfileFragment){
//                val action = communityProfileFragmentDirections.actionCommunityProfileFragmentToSearchCommunityFragment()
//                findNavController().navigate(action)
                navController.popBackStack()
            }
        }
    }

}