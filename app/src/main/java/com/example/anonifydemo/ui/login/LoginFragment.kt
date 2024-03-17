package com.example.anonifydemo.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentLoginBinding

import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.example.anonifydemo.ui.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class LoginFragment : Fragment(), Utils {

    private var _binding : FragmentLoginBinding? = null

    private val binding get() = _binding

    private val userViewModel : UserViewModel by activityViewModels()

//    private lateinit var viewModel: LoginViewModel

    private lateinit var loginBtn : Button

    private lateinit var signUpWithEmail : LinearLayout

    private lateinit var signUpWithGoogle : LinearLayout

    private lateinit var resultLauncher : ActivityResultLauncher<Intent>

    private lateinit var authUtil : AuthenticationUtil

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            try {
                if (result.resultCode == Activity.RESULT_OK){

                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    try {
                        val account = task.getResult(ApiException::class.java)!!
                        Log.d("Anonify : LoginFragment", account.email!!)
                        val idToken = account.idToken
                        goToSignUpFragment(account.email!!)
//                    firebaseAuthWithGoogle(account.idToken!!)

                    } catch (e : Exception){
                        Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_LONG).show()
                    }
                }else if (result.resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(requireContext(), result.resultCode, Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }

        }

        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        loginBtn = binding!!.loginbtn

        signUpWithEmail = binding!!.signUpWithEmail

        signUpWithGoogle = binding!!.signInWithGoogle

//        authUtil = AuthenticationUtil.getInstance(requireContext())


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

//        loginBtn.setOnClickListener {
//            goToSignInFragment()
//        }

        signUpWithEmail.setOnClickListener {
//            goToSignUpFragment()
            goToSignInFragment()
        }

        signUpWithGoogle.setOnClickListener {

            val signInIntent = mGoogleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
//            authUtil.signInWithGoogle(this, serverClientId = getString(R.string.web_client_id), onSuccess = { user ->
//                toast(requireContext(), "Welcome ${user.displayName}!")
////                setUser(user.uid, user.email)
//                goToChooseAvatarFragment()
//            }, onFailure = { e ->
//                handleFailure(requireContext(), e)
//            })
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

    private fun goToSignUpFragment(email: String) {

        if (findNavController().currentDestination!!.id == R.id.loginFragment){
            val action = LoginFragmentDirections.actionLoginFragmentToSignUpFragment(email = email)
            findNavController().navigate(action)
        }
    }

//    private fun setUser(uid: String, email: String?){
//        val user = User(uid, email)
//        userViewModel.setUser(user)
//    }


    companion object{
        const val TAG = "LoginFragment"
    }


}