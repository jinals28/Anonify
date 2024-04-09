package com.example.anonifydemo.ui.displayProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.launch

class Profile2ViewModel : ViewModel() {

    private val _postList = MutableLiveData<List<DisplayPost>>()

    val postList : LiveData<List<DisplayPost>> = _postList

    private val _currentUser = MutableLiveData<ActiveUser>()

    val currentUser : LiveData<ActiveUser> = _currentUser



    suspend fun getUser(userId: String) {

            _currentUser.value = AppRepository.getUser(userId)
            _postList.value = AppRepository.getUserPosts(userId)
    }
    // TODO: Implement the ViewModel
}