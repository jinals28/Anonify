package com.example.anonifydemo.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.repository.AppRepository

class NotificationsViewModel : ViewModel() {

    private val _currentUser = MutableLiveData<ActiveUser>()

    val currentUser : LiveData<ActiveUser> = _currentUser



    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    suspend fun getUser(userId: String) {

        _currentUser.value = AppRepository.getUser(userId)
    }
}