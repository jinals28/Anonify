package com.example.anonifydemo.ui.login

import android.credentials.GetCredentialRequest
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialProviderConfigurationException
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentLoginBinding
import com.example.anonifydemo.databinding.FragmentOnboardBinding
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null

    private val binding get() = _binding

//    private lateinit var viewModel: LoginViewModel

    private lateinit var loginBtn : Button

    private lateinit var signUpWithEmail : LinearLayout

    private lateinit var signUpWithGoogle : LinearLayout

    private lateinit var credentialManager : CredentialManager
    private val auth = com.google.firebase.ktx.Firebase.auth

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

        credentialManager = CredentialManager.create(requireContext())

        val googleIdOptions = GetSignInWithGoogleOption.Builder(getString(R.string.web_client_id))
            .build()

//        val googleIdOptions = GetGoogleIdOption.Builder()
//            .setServerClientId(getString(R.string.web_client_id))
//            .setFilterByAuthorizedAccounts(true)
//            .build()

        val request : androidx.credentials.GetCredentialRequest = androidx.credentials.GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptions)
            .build()

        val cancellationSignal = CancellationSignal()

        cancellationSignal.setOnCancelListener {

        }


        loginBtn.setOnClickListener {

            if (findNavController().currentDestination!!.id == R.id.loginFragment){
                findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
            }

        }

        signUpWithEmail.setOnClickListener {
            if (findNavController().currentDestination!!.id == R.id.loginFragment){
                val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }

        signUpWithGoogle.setOnClickListener {
            lifecycleScope.launch {
                try {

                    val result = credentialManager.getCredential(
                        request = request,
                        context = requireActivity()
                    )
                    handleSignIn(result)
                }catch(e: GetCredentialException){
                    handleFailure(e)
                }
            }

        }


    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.

        when(val credential = result.credential){

            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse on your server to
                // validate and authenticate
                val responseJson = credential.authenticationResponseJson
            }

            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password
            }

            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    try {

                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful){
                                    // Sign in success, update UI with the signed-in user's information
                                    val user = auth.currentUser
                                    toast("Welcome ${user!!.displayName}")
                                }else {
                                    // If sign in fails, display a message to the user.

                                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                                }
                            }
                    }catch (e : GoogleIdTokenParsingException){
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                }else{
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }

        }
    }
    private fun handleFailure(e: GetCredentialException) {
        when(e){
            is GetCredentialProviderConfigurationException -> {
                toast("No Google Account Found, Please Add a Gmail Account")
            }
            is GetCredentialCancellationException -> {

            }
            else -> {
                Log.e(TAG, "Exception $e")
            }
        }

    }

    fun toast(message : String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    companion object{
        const val TAG = "LoginFragment"
    }


}