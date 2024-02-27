package com.example.anonifydemo.ui.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentLoginBinding
import com.example.anonifydemo.databinding.FragmentOnboardBinding
import com.google.android.material.button.MaterialButton
import kotlin.math.log

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null

    private val binding get() = _binding

//    private lateinit var viewModel: LoginViewModel

    private lateinit var loginBtn : Button

    private lateinit var signUpWithEmail : LinearLayout

    private lateinit var signUpWithGoogle : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginBtn = binding!!.loginbtn

        signUpWithEmail = binding!!.signUpWithEmail

        signUpWithGoogle = binding!!.signInWithGoogle

        loginBtn.setOnClickListener {

            if (findNavController().currentDestination!!.id == R.id.loginFragment){
                findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
            }

        }

        signUpWithEmail.setOnClickListener {
            if (findNavController().currentDestination!!.id == R.id.loginFragment){
                val action = LoginFragmentDirections.actionLoginFragmentToSignInFragment()
                findNavController().navigate(action)
            }
        }

        signUpWithGoogle.setOnClickListener {

        }


    }



}