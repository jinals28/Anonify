package com.example.anonifydemo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.DisplaySaved
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _postList = MutableLiveData<List<DisplayPost>>()

    val list : LiveData<List<DisplayPost>> = _postList

    private val _savedList = MutableLiveData<List<DisplayPost>>()


    private val _currentUser = MutableLiveData<ActiveUser>()

    val currentUser : LiveData<ActiveUser> = _currentUser

    private var postList = mutableListOf<DisplayPost>()

    private var savedList = mutableListOf<DisplayPost>()

    suspend fun getUser(userId: String) {
        _postList.value = postList
        _currentUser.value = AppRepository.getUser(userId)
        postList = AppRepository.getUserPosts(userId).toMutableList()
        _postList.value = postList
        savedList = AppRepository.getSavedPosts(userId) as MutableList<DisplayPost>
    }

    fun getPost() {
        _postList.value = postList
    }

    fun getSavedPost(){
        _postList.value = savedList
    }

    fun deleteUser(userId: String) {
        viewModelScope.launch {
            AppRepository.deleteUser(userId)
        }
    }
}