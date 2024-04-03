package com.example.anonifydemo.ui.createPost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.utils.ValidationUtil
import kotlinx.coroutines.launch

class CreatePostViewModel : ViewModel() {

    private var topics: List<String> = listOf()

    private val _topicList = MutableLiveData<List<FollowingTopic>>()

    val topicList: LiveData<List<FollowingTopic>> = _topicList

//    private val postManager = PostManager.getInstance()

    init {


    }

//    fun set(topicList: List<String>) {
//
//        topics = topicList
//    }

    fun generateSuggestions(input: String) {
        Log.d("com.example.anonifydemo.ui.dataClasses.Post View Model", input)
        _topicList.value = AppRepository.followingTopicList.filter { it.topic.startsWith(input, ignoreCase = true) }.toList()
//        _topicList.value = AppRepository.getTopics().filter { it.name.startsWith(input, ignoreCase = true) }.toList()
        Log.d("com.example.anonifydemo.ui.dataClasses.Post View Model", _topicList.value.toString())
    }

    fun isValidContent(content: String): Boolean {
        return ValidationUtil.isValidPostContent(content)
    }

    fun isValidHashtag(hashtag: String): Boolean {
        // Implement hashtag validation logic
        // Check if the hashtag is in the predefined suggestion list
        return _topicList.value!!.any { it.topic.equals(hashtag, ignoreCase = true) }
//        return ValidationUtil.isHashtagValid(hashtag, topics) // Replace with your validation logic
    }

    private fun getHashtagId(hashtag: String): Long {
        return AppRepository.getHashtagId(hashtag)
    }

    fun addPost(userId: String, content: String, hashtag: String) {
       val post =  Post(
           userId = userId,
           topicName = hashtag,
           postContent = content,
           postCreatedAt = System.currentTimeMillis()
           )
        viewModelScope.launch {
            AppRepository.addPost(post)
        }


    }

    fun getAvatarId(avatarId: Long): Int {
        return 0
    }

    fun setTopicList(followingTopics: List<FollowingTopic>) {

        _topicList.value = followingTopics

    }


}