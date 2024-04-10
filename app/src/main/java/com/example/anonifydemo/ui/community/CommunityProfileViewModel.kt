package com.example.anonifydemo.ui.community

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.dataClasses.DisplayCommunity
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.repository.AppRepository
import kotlinx.coroutines.launch

class CommunityProfileViewModel : ViewModel() {

    private val _postList = MutableLiveData<List<DisplayPost>>()

    val postList : LiveData<List<DisplayPost>> = _postList

    private val _currentCommunity = MutableLiveData<DisplayCommunity>()

    val currentCommunity : LiveData<DisplayCommunity> = _currentCommunity
    suspend fun getCommunity(userId : String, topicName: String) {

        _currentCommunity.value = AppRepository.getCommunity(userId = userId, topicName = topicName)
        _postList.value = AppRepository.getCommunityPosts(userId = userId, topicName = topicName)
    }

    fun followCommunity(userId: String, topicName: FollowingTopic) {
        viewModelScope.launch {
            AppRepository.followCommunity(userId, topicName)
        }
    }

    fun unfollowCommunity(userId: String, topicName: FollowingTopic) {
        viewModelScope.launch {
            AppRepository.unfollowCommunity(userId, topicName)
        }

    }

}