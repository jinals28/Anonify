package com.example.anonifydemo.ui.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.dataClasses.User
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.example.anonifydemo.ui.utils.Utils
import com.example.anonifydemo.ui.utils.ValidationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel(), Utils {

    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid: LiveData<Boolean> = _isEmailValid

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean> = _isPasswordValid

    private val _isSuccessful = MutableLiveData<Pair<Boolean, User>>()
    val isSuccessful : LiveData<Pair<Boolean, User>> = _isSuccessful

    private val _isFailure = MutableLiveData<Exception>()
    val isFailure : LiveData<Exception> = _isFailure

    private lateinit var auth : AuthenticationUtil

    init {
        // Default values
        _isEmailValid.value = true
        _isPasswordValid.value = true
    }
    fun validateFields(email: String, password: String): Boolean {

        _isEmailValid.value = ValidationUtil.isValidEmail(email)
        _isPasswordValid.value = ValidationUtil.isValidPassword(password)


        return _isEmailValid.value!! && _isPasswordValid.value!!
    }

    private val _signInResult = MutableLiveData<Pair<Boolean, Pair<User, List<FollowingTopic>>>>()
    val signInResult: LiveData<Pair<Boolean, Pair<User, List<FollowingTopic>>>>
        get() = _signInResult

    private val _signInError = MutableLiveData<Exception>()
    val signInError: LiveData<Exception>
        get() = _signInError

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun signInWithEmailAndPassword(context: Context, email: String, password: String) {
        log("in sign in viewmodel, signinwithemailandpassword")
        coroutineScope.launch {
            try {
                val firebaseUser = AuthenticationUtil.signInWithEmailAndPassword(email, password)
                val pair= AppRepository.getUserByUid(firebaseUser.uid)
                _signInResult.value = Pair(true, pair)
            } catch (e: Exception) {
                _signInError.value = e
            }
        }
    }

    fun fetchPosts(followingTopicsList : List<FollowingTopic>) {
        viewModelScope.launch {
            AppRepository.fetchPosts(followingTopicsList)
        }


    }

//    fun signInWithEmailPassword(context : Context, email: String, password: String) {
//        auth = AuthenticationUtil.getInstance(context)
//        auth.signInWithEmailAndPassword(email = email, password = password, onSuccess = { firebaseUser ->
//            val user = AppRepository.getUser(firebaseUser.uid)
//            _isSuccessful.value = Pair(true, user!!)
//        }, onFailure = {e->
//            _isFailure.value = e
//        })
//    }
    // TODO: Implement the ViewModel
}