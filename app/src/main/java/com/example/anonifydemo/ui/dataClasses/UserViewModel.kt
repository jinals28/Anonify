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

    val followingTopicList : MutableList<FollowingTopic> = mutableListOf()

    private val _followingTopicList = MutableLiveData<FollowingTopic>()

    val followingTopic : LiveData<FollowingTopic> = _followingTopicList

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
    val updatedUser = currentUser.copy(avatar = avatar)
    _user.value = updatedUser
    viewModelScope.launch {
        AppRepository.updateUserAvatar(updatedUser.uid, avatar.name)
    }
    // Save updated user data to Firestore
}

    fun saveSelectedTopics(selectedTopics: List<FollowingTopic>) {
        viewModelScope.launch {
            // Save selected topics to Firestore
            AppRepository.saveSelectedTopics(selectedTopics, _user.value!!.uid)

            // Update the local cache
            val currentUser = _user.value ?: ActiveUser()
            val updatedUser = currentUser.copy(followingTopics = selectedTopics)
            _user.value = updatedUser
        }
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

    fun getUserId() : String {
        return  _user.value!!.uid
    }
}

