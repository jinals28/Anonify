package com.example.anonifydemo.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.repository.AppRepository

class HomeViewModel : ViewModel() {

    private val _followingTopicList = MutableLiveData<List<FollowingTopic>>()

    val followingTopicList : LiveData<List<FollowingTopic>> = _followingTopicList

    private val _postList = MutableLiveData<List<DisplayPost>>()

    val postList : LiveData<List<DisplayPost>> = _postList

    private val _followingTopicIdList = MutableLiveData<List<Long>>()

    val followingTopicIdList : LiveData<List<Long>> = _followingTopicIdList
    fun getFollowingTopicList(userId: Long) {
//        _followingTopicList.value = AppRepository.getFollowingTopicList(userId)
//        getFollowingTopicIdList(userId)
    }

//    private fun getFollowingTopicIdList(userId: Long) {
//        _followingTopicIdList.value = AppRepository.getFollowingTopicIdListForUser(userId)
//
//    }

  fun getPostsForUser(userId: Long){
      Log.d("Anonify : homeVm", "HomeVM $userId")
        _postList.value = AppRepository.getDisplayPostsForUser(userId)
    }

    init {


    }
}