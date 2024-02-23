package com.example.anonifydemo.ui.signin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentLoginBinding
import com.example.anonifydemo.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {


    private var _binding : FragmentSignInBinding? = null

    private val binding get() = _binding

    private lateinit var btnSignIn : Button

    //private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSignIn = binding!!.btnSignIn

        btnSignIn.setOnClickListener {

            if (findNavController().currentDestination!!.id == R.id.signUpFragment){
                findNavController().navigate(R.id.action_signUpFragment_to_chooseAvatarFragment)
            }

        }
    }

}