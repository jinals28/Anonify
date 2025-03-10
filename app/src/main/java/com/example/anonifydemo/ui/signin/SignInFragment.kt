package com.example.anonifydemo.ui.signin

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentSignInBinding
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.example.anonifydemo.ui.utils.Utils
import com.google.android.gms.common.SignInButton
import io.grpc.Context


class SignInFragment : Fragment(), Utils {

    private var _binding: FragmentSignInBinding? = null

    private val binding get() = _binding

    private lateinit var btnSignIn: Button

    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var signUpTxt: TextView

    private lateinit var txtEmail: EditText

    private lateinit var txtPassword: EditText
   // private lateinit var rememberMe:CheckBox

    private lateinit var authUtil: AuthenticationUtil

    private lateinit var signInWithGoogle: SignInButton

    private lateinit var viewModel: SignInViewModel
    private lateinit var ForgetPass: TextView

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

      //  rememberMe=binding!!.RememberMe
//        signInWithGoogle = binding!!.googleSignInBtn

        txtEmail = binding!!.txtemail

        txtPassword = binding!!.txtPassword
        ForgetPass = binding!!.ForgetPass

//        authUtil = AuthenticationUtil.getInstance(requireContext())

        observeLivedata()

        btnSignIn.setOnClickListener {
            try{
                val email = txtEmail.text.toString()
                val password = txtPassword.text.toString()
               // val rememberMe = rememberMe.isChecked
                log("SignInFragment")
                if (viewModel.validateFields(email, password)) {
                    viewModel.signInWithEmailAndPassword(requireContext(), email, password)
                }
            }catch (e : Exception){
                log(e.toString())
            }

        }

        signUpTxt.setOnClickListener {

            goToSignUpFragment()
        }

        ForgetPass.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val dialogView = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)
            val btnreset = dialogView.findViewById<Button>(R.id.btnreset)
            builder.setView(dialogView)
            val dialog = builder.create()

            btnreset.setOnClickListener {
                val userEmail = emailBox.text.toString()
                if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail)
                        .matches()
                ) {
                    toast(requireContext(), "Enter your registered email id")
                }
                AuthenticationUtil.sendPasswordResetEmail(userEmail,
                    onSuccess = {
                        toast(requireContext(), "Check your email")
                        dialog.dismiss()
                    },
                    onFailure = { exception ->
                        toast(requireContext(), "Unable to send ..failed")
                    }
                )

            }
            dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }
            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.show()

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
            viewModel.signInResult.observe(viewLifecycleOwner) {
                val list = it.second.second
                val avatar = if (it.second!!.first.avatar != "") {
                    AppRepository.getAvatar(it.second!!.first.avatar)
                } else {
                    Avatar()
                }
                val user = ActiveUser(
                    uid = it.second!!.first.uid,
                    email = it.second!!.first.email,
                    createdAt = it.second!!.first.createdAt,
                    avatar = avatar,
                    followingTopics = list,
                    followingTopicsCount = list.size.toLong()
                )
                userViewModel.setUser(user)
                storeActiveUser(user)
                toast(requireContext(), "Welcome User!!")

                //TODO: AFTER SIGNING IN IF UID IS PRESENT THEN CHECK IF AVATAR AND TOPIC IS SET OR NOT THEN NAVIGATE
                if (it.second!!.first.avatar != "") {

                    if (it.second!!.second.isNotEmpty()) {
                        viewModel.fetchPosts(it.second.first.uid, it.second.second)
                        goToHomeFragment()
                    }

                    else {

                        goToChooseTopicFragment()

                    }

                } else {
                    goToChooseAvatarFragment()
                }

            }

            viewModel.isFailure.observe(viewLifecycleOwner) { e ->
                handleFailure(requireContext(), e)
            }
        }

    // Storing the active user data in SharedPreferences
    fun storeActiveUser(user: ActiveUser) {
        val sharedPreferences = requireContext().getSharedPreferences("user_data", android.content.Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("uid", user.uid)
//        editor.putString("email", user.email)
//        editor.putLong("createdAt", user.createdAt)
//        editor.putString("avatarName", user.avatar.name)
//        editor.putInt("avatarUrl", user.avatar.url)
//        val followingTopicsSet = user.followingTopics.map { it.topic }.toSet()
//        editor.putStringSet("followingTopics", followingTopicsSet)
//        editor.putLong("followingTopicsCount", user.followingTopicsCount)
        editor.apply()
    }

    private fun goToHomeFragment() {
            if (findNavController().currentDestination!!.id == R.id.signInFragment) {
                val action = SignInFragmentDirections.actionSignInFragmentToNavigationHome()
                findNavController().navigate(action)
            }
        }
        private fun goToChooseAvatarFragment() {
            if (findNavController().currentDestination!!.id == R.id.signInFragment) {
                val action = SignInFragmentDirections.actionSignInFragmentToChooseAvatarFragment()
                findNavController().navigate(action)
            }
        }
        private fun goToChooseTopicFragment() {
            if (findNavController().currentDestination!!.id == R.id.signInFragment) {
                val action = SignInFragmentDirections.actionSignInFragmentToChooseTopic()
                findNavController().navigate(action)
            }
        }
        private fun goToSignUpFragment() {
            if (findNavController().currentDestination!!.id == R.id.signInFragment) {
                val action =
                    SignInFragmentDirections.actionSignInFragmentToSignUpFragment(email = "")
                findNavController().navigate(action)
            }
        }
}


//    signInWithGoogle.setOnClickListener {
//
////            authUtil.signInWithGoogle(this, getString(R.string.web_client_id), onSuccess = { user ->
////                toast(requireContext(), "Welcome ${user.displayName}")
//////               setUser(user.uid, user.email)
////                goToChooseAvatarFragment()
////
////            }, onFailure = { e ->
////                handleFailure(requireContext(), e)
////            })
//        }

//    private fun setUser(uid: String, email: String?){
//        val user = User(uid, email)
//        userViewModel.setUser(user)
//    }