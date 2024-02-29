package com.example.anonifydemo.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialProviderConfigurationException
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentLoginBinding
import com.example.anonifydemo.ui.util.AuthenticationUtil
import com.example.anonifydemo.ui.util.Utils
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth

class LoginFragment : Fragment(), Utils {

    private var _binding : FragmentLoginBinding? = null

    private val binding get() = _binding

//    private lateinit var viewModel: LoginViewModel

    private lateinit var loginBtn : Button

    private lateinit var signUpWithEmail : LinearLayout

    private lateinit var signUpWithGoogle : LinearLayout

    private lateinit var authUtil : AuthenticationUtil

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

        authUtil = AuthenticationUtil(requireContext())



        loginBtn.setOnClickListener {
            goToSignInFragment()
        }

        signUpWithEmail.setOnClickListener {
            goToSignUpFragment()
        }

        signUpWithGoogle.setOnClickListener {

            authUtil.signInWithGoogle(this, serverClientId = getString(R.string.web_client_id), onSuccess = { user ->
                toast(requireContext(), "Welcome ${user.displayName}!")
                goToChooseAvatarFragment()
            }, onFailure = { e ->
                handleFailure(requireContext(), e)
            })
        }
    }

    private fun goToChooseAvatarFragment() {

        if (findNavController().currentDestination!!.id == R.id.loginFragment){
            val action = LoginFragmentDirections.actionLoginFragmentToChooseAvatarFragment()
            findNavController().navigate(action)
        }
    }

    private fun goToSignInFragment(){
        if (findNavController().currentDestination!!.id == R.id.loginFragment){
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }
    }

    private fun goToSignUpFragment(){
        if (findNavController().currentDestination!!.id == R.id.loginFragment){
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }


    companion object{
        const val TAG = "LoginFragment"
    }


}