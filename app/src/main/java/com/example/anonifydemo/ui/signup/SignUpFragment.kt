package com.example.anonifydemo.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentSignUpBinding
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption

class SignUpFragment : Fragment() {

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: SignUpViewModel

    private lateinit var signInTxt : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInTxt = binding!!.txtLogIn

        signInTxt.setOnClickListener {

            if (findNavController().currentDestination!!.id == R.id.signUpFragment){

                val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                findNavController().navigate(action)

            }
        }

    }

}