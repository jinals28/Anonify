package com.example.anonifydemo.ui.dataClasses

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.launch

class  UserViewModel : ViewModel() {

    private val _user = MutableLiveData<ActiveUser>()

    val user : LiveData<ActiveUser> = _user

    val followingTopicList : MutableList<FollowingTopic> = mutableListOf()

    private val _followingTopicList = MutableLiveData<List<FollowingTopic>>()

    val followingTopic : LiveData<List<FollowingTopic>> = _followingTopicList

    fun setUser(user : ActiveUser){
        Log.d("Anonify : UserViewModel", user.toString())
        _followingTopicList.value = user.followingTopics
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
            _followingTopicList.value = selectedTopics
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

    fun addTopic(topicName: FollowingTopic) {
        // Get the current list of following topics
        val currentTopics = _followingTopicList.value?.toMutableList() ?: mutableListOf()

        // Create a new following topic with the given name and a dummy timestamp

        // Add the new topic to the list
        currentTopics.add(topicName)

        // Update the LiveData with the new list of following topics
        _followingTopicList.value = currentTopics

        // Also, update the user object if needed
        val currentUser = _user.value
        if (currentUser != null) {
            val updatedUser = currentUser.copy(followingTopics = currentTopics)
            _user.value = updatedUser
        }

    }

    fun removeTopic(topicName: String) {

        val currentTopics = _followingTopicList.value?.toMutableList() ?: mutableListOf()

        // Find the index of the topic to be deleted
        val index = currentTopics.indexOfFirst { it.topic == topicName }

        // If the topic is found, remove it from the list
        if (index != -1) {
            currentTopics.removeAt(index)

            // Update the LiveData with the modified list of following topics
            _followingTopicList.value = currentTopics

            // Also, update the user object if needed
            val currentUser = _user.value
            if (currentUser != null) {
                val updatedUser = currentUser.copy(followingTopics = currentTopics)
                _user.value = updatedUser
            }
        }
    }
}

