package com.example.anonifydemo.ui.dataClasses

import android.adservices.topics.Topic
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.repository.AppRepository

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User>()

    val user : LiveData<User> = _user

    fun setUser(user : User){

        _user.value = user
    }

    fun updateUserAvatarUrl(avatarId: Long) {
        val currentUser = _user.value ?: User()
        val updatedUser = currentUser.copy(avatarId = avatarId)
        _user.value = updatedUser
        Log.d("Anonify : UserView Model", updatedUser.toString())
        AppRepository.updateUser(updatedUser)

    }
//
//    fun updateUserTopic(topicList: List<FollowingTopic>) {
//        val currentUser = _user.value ?: User()
//        val updatedUser = currentUser.copy(topics = topicList.toMutableList())
//        _user.value = updatedUser
//    }

    fun getUser(): User? {
        return _user.value
    }

    fun getUserId() : Long {
        return  1L
    }
}

