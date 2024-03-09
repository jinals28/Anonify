package com.example.anonifydemo.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentSignUpBinding
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.example.anonifydemo.ui.utils.Utils
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.material.button.MaterialButton

class SignUpFragment : Fragment(), Utils {

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: SignUpViewModel

    private lateinit var signInTxt : TextView

    private lateinit var txtEmail : EditText

    private lateinit var txtPassword : EditText

    private lateinit var txtConPassword : EditText

    private lateinit var btnSignUp : Button

    private lateinit var auth : AuthenticationUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        auth = AuthenticationUtil.getInstance(requireContext())

        signInTxt = binding!!.txtLogIn

        txtEmail = binding!!.txtEmail

        txtPassword = binding!!.txtPassword

        txtConPassword = binding!!.txtConPassword

        btnSignUp = binding!!.btnSignUp

        observeValidation()

        btnSignUp.setOnClickListener {

            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()
            val conPass = txtConPassword.text.toString()

            if (viewModel.validateFields(email = email, password = password, conPass = conPass)){
                viewModel.signUpWithEmailAndPassword(requireContext(), email, password)
            }

        }



        signInTxt.setOnClickListener {

           goToSignInFragment()
        }
    }

    private fun observeValidation() {
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

        viewModel.isConfirmPasswordValid.observe(viewLifecycleOwner) { isValid ->
            // Update UI based on confirm password validation
            if (!isValid) {
                txtConPassword.error = "Passwords do not match"
            } else {
                txtConPassword.error = null
            }
        }

        viewModel.isSuccessful.observe(viewLifecycleOwner){
            toast(requireContext(), "Account Successfully Created!!")
            goToSignInFragment()
        }

        viewModel.isFailure.observe(viewLifecycleOwner){e ->
            handleFailure(requireContext(), e)
        }
    }

    private fun goToSignInFragment(){
        if (findNavController().currentDestination!!.id == R.id.signUpFragment){

            val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            findNavController().navigate(action)

        }
    }

}