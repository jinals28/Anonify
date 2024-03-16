package com.example.anonifydemo.ui.utils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.launch

class AuthenticationUtil private constructor(private val context : Context) : Utils {

    companion object {
        private const val TAG = "AuthenticationUtil"

        private val auth = com.google.firebase.ktx.Firebase.auth

        fun getInstance(context: Context) : AuthenticationUtil{
            return AuthenticationUtil(context)
        }

    }

    private val credentialManager = CredentialManager.create(context)

    private fun handleSignIn(result: GetCredentialResponse, onSuccess: (FirebaseUser) -> Unit) {

        // Handle the successfully returned credential.

        when (val credential = result.credential) {

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
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {

                        // Use googleIdTokenCredential and extract id to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val firebaseCredential =
                            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    val user = auth.currentUser
                                    onSuccess(user!!)
                                } else if (task.isCanceled){
                                    // If sign in fails, display a message to the user.

                                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                                }else if (task.isComplete){
                                    Log.w(TAG, task.exception)
                                }else {
                                    Log.w(TAG, task.exception)
                                }
                            }

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
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

    fun signInWithGoogle(
        fragment: Fragment,
        serverClientId: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {

        val googleIdOptions = GetSignInWithGoogleOption.Builder(serverClientId)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptions)
            .build()

        fragment.lifecycleScope.launch {
            try {

                val result = credentialManager.getCredential(
                    request = request,
                    context = fragment.requireActivity()
                )
                handleSignIn(result, onSuccess)

            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(auth.currentUser!!)
                    auth.signOut()
                } else {
                    onFailure(task.exception!!)
                }

            }
    }

    fun signInWithEmailAndPassword(email: String,
                                   password: String,
                                   onSuccess: (FirebaseUser) -> Unit,
                                   onFailure: (Exception) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    onSuccess(auth.currentUser!!)
                }else{
                    onFailure(task.exception!!)
                }
            }


    }
    fun sendPasswordResetEmail(userEmail: String,
                               onSuccess: () -> Unit,
                               onFailure: (Exception) -> Unit) {
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception!!)
            }
        }
    }

}