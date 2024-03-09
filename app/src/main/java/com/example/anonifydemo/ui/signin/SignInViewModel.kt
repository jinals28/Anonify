package com.example.anonifydemo.ui.signin

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.example.anonifydemo.ui.utils.ValidationUtil

class SignInViewModel : ViewModel() {

    private val _isEmailValid = MutableLiveData<Boolean>()
    val isEmailValid: LiveData<Boolean> = _isEmailValid

    private val _isPasswordValid = MutableLiveData<Boolean>()
    val isPasswordValid: LiveData<Boolean> = _isPasswordValid

    private val _isSuccessful = MutableLiveData<Boolean>()
    val isSuccessful : LiveData<Boolean> = _isSuccessful

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


    fun signInWithEmailPassword(context : Context, email: String, password: String) {
        auth = AuthenticationUtil.getInstance(context)
        auth.signInWithEmailAndPassword(email = email, password = password, onSuccess = {
            _isSuccessful.value = true
        }, onFailure = {e->
            _isFailure.value = e
        })
    }
    // TODO: Implement the ViewModel
}