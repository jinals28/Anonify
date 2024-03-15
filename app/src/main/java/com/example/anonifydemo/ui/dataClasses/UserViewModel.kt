package com.example.anonifydemo.ui.dataClasses

import android.adservices.topics.Topic
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user : LiveData<User> = _user

    fun setUser(user : User){

        _user.value = user
    }

    fun updateUserAvatarUrl(avatarUrl: Avatar) {
        val currentUser = _user.value ?: User()
        val updatedUser = currentUser.copy(avatarUrl = avatarUrl)
        _user.value = updatedUser
    }

    fun updateUserTopic(topicList: List<Topics>) {
        val currentUser = _user.value ?: User()
        val updatedUser = currentUser.copy(topics = topicList.toMutableList())
        _user.value = updatedUser
    }

    fun getUser(): User? {
        return _user.value
    }
}
