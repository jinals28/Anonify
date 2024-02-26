    package com.example.anonifydemo.ui.ChooseAvatar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseAvatarBinding
import com.example.anonifydemo.databinding.FragmentLoginBinding

    class chooseAvatarFragment : Fragment() {

    private var _binding : FragmentChooseAvatarBinding? = null

    private val binding get() = _binding

    //private lateinit var viewModel: ChooseAvatarViewModel
    private lateinit var btnavatar: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseAvatarBinding.inflate(layoutInflater, container, false)

        return binding!!.root    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            btnavatar = binding!!.btnavatar
            btnavatar.setOnClickListener{
                if(findNavController().currentDestination!!.id==R.id.chooseAvatarFragment){
                    findNavController().navigate(R.id.action_chooseAvatarFragment_to_chooseTopic)

                }
            }
        }
    }