package com.example.anonifydemo.ui.dataClasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<ActiveUser>()

    val user : LiveData<ActiveUser> = _user

    fun setUser(user : ActiveUser){

        _user.value = user
    }

//    fun updateUserAvatarUrl(avatarId: Long) {
//        val currentUser = _user.value ?: User()
//        val updatedUser = currentUser.copy(avatarId = avatarId)
//        _user.value = updatedUser
//        Log.d("Anonify : UserView Model", updatedUser.toString())
//        AppRepository.updateUser(updatedUser)
//
//    }
fun updateUserAvatarUrl(avatar: Avatar) {
    val currentUser = _user.value ?: ActiveUser()
    val updatedUser = currentUser.copy(avatarId = Avatar())
    _user.value = updatedUser
    viewModelScope.launch {
        AppRepository.updateUserAvatar(updatedUser.uid, avatar)
    }
    // Save updated user data to Firestore
}
//
//    fun updateUserTopic(topicList: List<FollowingTopic>) {
//        val currentUser = _user.value ?: User()
//        val updatedUser = currentUser.copy(topics = topicList.toMutableList())
//        _user.value = updatedUser
//    }

    fun getUser(): ActiveUser? {
        return _user.value
    }

    fun getUserId() : Long {
        return  1L
    }
}

