package com.example.anonifydemo.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentSignInBinding
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.example.anonifydemo.ui.utils.Utils
import com.google.android.gms.common.SignInButton

class SignInFragment : Fragment(), Utils{


    private var _binding : FragmentSignInBinding? = null

    private val binding get() = _binding

    private lateinit var btnSignIn : Button

    private lateinit var signUpTxt : TextView

    private lateinit var txtEmail : EditText

    private lateinit var txtPassword : EditText

    private lateinit var authUtil : AuthenticationUtil

    private lateinit var signInWithGoogle : SignInButton

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        btnSignIn = binding!!.btnSignIn

        signUpTxt = binding!!.txtSignUp

        signInWithGoogle = binding!!.googleSignInBtn

        txtEmail = binding!!.txtemail

        txtPassword = binding!!.txtPassword

        authUtil = AuthenticationUtil.getInstance(requireContext())

        observeLivedata()

        btnSignIn.setOnClickListener {

            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()

            if(viewModel.validateFields(email, password)){
                viewModel.signInWithEmailPassword(requireContext(), email, password)
            }
        }

        signUpTxt.setOnClickListener {

            goToSignUpFragment()
        }

        signInWithGoogle.setOnClickListener {

            authUtil.signInWithGoogle(this, getString(R.string.web_client_id), onSuccess = { user ->
                toast(requireContext(), "Welcome ${user.displayName}")
                goToChooseAvatarFragment()

            }, onFailure = { e ->
                handleFailure(requireContext(), e)
            })
        }
    }

    private fun observeLivedata() {
        viewModel.isEmailValid.observe(viewLifecycleOwner) { isValid ->
            // Update UI based on email validation
            if (!isValid) {
                txtEmail.error = "Invalid email"
            } else {
                txtEmail.error = null
            }
        }

        viewModel.isPasswordValid.observe(viewLifecycleOwner) { isValid ->
            // Update UI based on password validation
            if (!isValid) {
                txtPassword.error = "Invalid password"

            } else {
                txtPassword.error = null
            }
        }

        viewModel.isSuccessful.observe(viewLifecycleOwner){
            toast(requireContext(), "Welcome User!!")
            goToChooseAvatarFragment()
        }

        viewModel.isFailure.observe(viewLifecycleOwner){e ->
            handleFailure(requireContext(), e)
        }
    }

    private fun goToChooseAvatarFragment(){
        if (findNavController().currentDestination!!.id == R.id.signInFragment){
            val action = SignInFragmentDirections.actionSignInFragmentToChooseAvatarFragment()
            findNavController().navigate(action)
        }
    }

    private fun goToSignUpFragment(){
        if (findNavController().currentDestination!!.id == R.id.signInFragment){
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }


}