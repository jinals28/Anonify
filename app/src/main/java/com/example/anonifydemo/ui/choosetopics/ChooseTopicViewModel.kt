package com.example.anonifydemo.ui.choosetopics

import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.repository.AppRepository

class ChooseTopicViewModel : ViewModel() {

    fun addFollowingTopicList(followingTopicList : List<FollowingTopic>){
        followingTopicList.forEach {
            AppRepository.addFollowingTopic(followingTopic = it)
        }
    }
}