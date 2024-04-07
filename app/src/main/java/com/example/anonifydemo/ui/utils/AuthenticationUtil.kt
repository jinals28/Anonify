package com.example.anonifydemo.ui.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.anonifydemo.ui.dataClasses.User
//import android.util.Log
//import androidx.credentials.CredentialManager
//import androidx.credentials.CustomCredential
//import androidx.credentials.GetCredentialRequest
//import androidx.credentials.GetCredentialResponse
//import androidx.credentials.PasswordCredential
//import androidx.credentials.PublicKeyCredential
//import androidx.credentials.exceptions.GetCredentialException
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.lifecycleScope
//import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
//import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
//import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//class AuthenticationUtil private constructor(private val context : Context) : Utils {
//
//    companion object {
//        private const val TAG = "AuthenticationUtil"
//
//        private val auth = com.google.firebase.ktx.Firebase.auth
//
//        fun getInstance(context: Context) : AuthenticationUtil{
//            return AuthenticationUtil(context)
//        }
//
//    }
//
////    private val credentialManager = CredentialManager.create(context)
////
////    private fun handleSignIn(result: GetCredentialResponse, onSuccess: (FirebaseUser) -> Unit) {
////
////        // Handle the successfully returned credential.
////
////        when (val credential = result.credential) {
////
////            is PublicKeyCredential -> {
////                // Share responseJson such as a GetCredentialResponse on your server to
////                // validate and authenticate
////                val responseJson = credential.authenticationResponseJson
////            }
////
////            is PasswordCredential -> {
////                // Send ID and password to your server to validate and authenticate.
////                val username = credential.id
////                val password = credential.password
////            }
////
////            is CustomCredential -> {
////                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
////                    try {
////
////                        // Use googleIdTokenCredential and extract id to validate and
////                        // authenticate on your server.
////                        val googleIdTokenCredential = GoogleIdTokenCredential
////                            .createFrom(credential.data)
////                        val firebaseCredential =
////                            GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
////                        auth.signInWithCredential(firebaseCredential)
////                            .addOnCompleteListener { task ->
////                                if (task.isSuccessful) {
////                                    // Sign in success, update UI with the signed-in user's information
////                                    val user = auth.currentUser
////                                    onSuccess(user!!)
////
////                                } else if (task.isCanceled){
////                                    // If sign in fails, display a message to the user.
////
////                                    Log.w(TAG, "signInWithCredential:failure", task.exception)
////                                }else if (task.isComplete){
////                                    Log.w(TAG, task.exception)
////                                }else {
////                                    Log.w(TAG, task.exception)
////                                }
////                            }
////
////                    } catch (e: GoogleIdTokenParsingException) {
////                        Log.e(TAG, "Received an invalid google id token response", e)
////                    }
////                } else {
////                    // Catch any unrecognized custom credential type here.
////                    Log.e(TAG, "Unexpected type of credential")
////                }
////            }
////
////            else -> {
////                // Catch any unrecognized credential type here.
////
////                Log.e(TAG, "Unexpected type of credential")
////            }
////
////        }
////    }
////
////    fun signInWithGoogle(
////        fragment: Fragment,
////        serverClientId: String,
////        onSuccess: (FirebaseUser) -> Unit,
////        onFailure: (Exception) -> Unit
////    ) {
////
////        val googleIdOptions = GetSignInWithGoogleOption.Builder(serverClientId)
////            .build()
////
////        val request: GetCredentialRequest = GetCredentialRequest.Builder()
////            .addCredentialOption(googleIdOptions)
////            .build()
////
////        fragment.lifecycleScope.launch {
////            try {
////
////                val result = credentialManager.getCredential(
////                    request = request,
////                    context = fragment.requireActivity()
////                )
////                handleSignIn(result, onSuccess)
////
////            } catch (e: Exception) {
////                onFailure(e)
////            }
////        }
////    }
//

////        auth.createUserWithEmailAndPassword(email, password)
////            .addOnCompleteListener { task ->
////                if (task.isSuccessful) {
////                    onSuccess(auth.currentUser!!)
////                    return User = (
////
////                            )
////                    auth.signOut()
////                } else {
////                    onFailure(task.exception!!)
////                }
////
////            }
//    }
//
//     fun signInWithEmailAndPassword(email: String,
//                                   password: String,
//                                   onSuccess: (FirebaseUser) -> Unit,
//                                   onFailure: (Exception) -> Unit) {
//        auth.signInWithEmailAndPassword(email, password)
//            .addOnCompleteListener{task ->
//                if (task.isSuccessful){
//                    onSuccess(auth.currentUser!!)
//                }else{
//                    onFailure(task.exception!!)
//                }
//            }
//
//
//    }

//    }
//
//}
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AuthenticationUtil : Utils {
    private const val PREF_NAME = "AuthPrefs"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
  private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isLoggedIn(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun saveCredentials(context: Context, email: String, password: String) {
        getSharedPreferences(context).edit()
            
            .putString(KEY_EMAIL, email)
            .putString(KEY_PASSWORD, password)
            .apply()
    }

    fun clearCredentials(context: Context) {
        getSharedPreferences(context).edit()
            .remove(KEY_EMAIL)
            .remove(KEY_PASSWORD)
            .apply()
    }

//    suspend fun signInWithEmailAndPassword(context: Context, email: String, password: String): Boolean {
//        return withContext(Dispatchers.IO) {
//            try {
//                val authResult = auth.signInWithEmailAndPassword(email, password).await()
//                val loggedIn = authResult.user != null
//                if (loggedIn) {
//                    saveCredentials(context, email, password)
//                    setLoggedIn(context, true)
//                }
//                authResult.user ?: throw IllegalStateException("User not found")
//                loggedIn
//            } catch (e: Exception) {
//                false
//            }
//        }
//    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): FirebaseUser {
        return withContext(Dispatchers.IO) {
            log("AuthenticationUtil, signInWithEmailAndPassword")
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user ?: throw IllegalStateException("User not found")
        }
    }

    suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
        onSuccess: (FirebaseUser) -> Unit,
        onFailure: (Exception) -> Unit
    ): User {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val currentUser = authResult.user ?: throw IllegalStateException("User is null")
        return User(
            uid = currentUser.uid,
            email = currentUser.email ?: "",
            createdAt = System.currentTimeMillis()
        )

    }

//    suspend fun signUpWithEmailAndPassword(
//        context: Context,
//        email: String,
//        password: String,
//        onSuccess: (FirebaseUser) -> Unit,
//        onFailure: (Exception) -> Unit
//    ): User {
//        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
//        val currentUser = authResult.user ?: throw IllegalStateException("User is null")
//        saveCredentials(context, email, password)
//        setLoggedIn(context, true)
//        return User(
//            uid = currentUser.uid,
//            email = currentUser.email ?: "",
//            createdAt = System.currentTimeMillis()
//        )
//    }

    fun sendPasswordResetEmail(
        userEmail: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onFailure(task.exception!!)
            }
        }
    }

    fun logout(context: Context) {
        auth.signOut()
        clearCredentials(context)
        setLoggedIn(context, false)
    }

    fun clearRememberMe(context: Context) {
        clearCredentials(context)
        setLoggedIn(context, false)
    }
}
