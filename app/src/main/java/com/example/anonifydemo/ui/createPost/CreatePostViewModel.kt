package com.example.anonifydemo.ui.createPost

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.utils.ValidationUtil

class CreatePostViewModel : ViewModel() {

    private var topics: List<String> = listOf()

    private val _topicList = MutableLiveData<List<Topic>>()

    val topicList: LiveData<List<Topic>> = _topicList

//    private val postManager = PostManager.getInstance()

    init {

        _topicList.value = AppRepository.getTopics()
    }

//    fun set(topicList: List<String>) {
//
//        topics = topicList
//    }

    fun generateSuggestions(input: String) {
        Log.d("com.example.anonifydemo.ui.dataClasses.Post View Model", input)
        _topicList.value = AppRepository.getTopics().filter { it.name.startsWith(input, ignoreCase = true) }.toList()
        Log.d("com.example.anonifydemo.ui.dataClasses.Post View Model", _topicList.value.toString())
    }

    fun isValidContent(content: String): Boolean {
        return ValidationUtil.isValidPostContent(content)
    }

    fun isValidHashtag(hashtag: String): Boolean {
        // Implement hashtag validation logic
        // Check if the hashtag is in the predefined suggestion list
        return _topicList.value!!.any { it.name.equals(hashtag, ignoreCase = true) }
//        return ValidationUtil.isHashtagValid(hashtag, topics) // Replace with your validation logic
    }

    private fun getHashtagId(hashtag: String): Long {
        return AppRepository.getHashtagId(hashtag)
    }

    fun addPost(userId: Long, content: String, hashtag: String) {
        val topicId = getHashtagId(hashtag)
       val post =  Post(userId = userId,
           postId = AppRepository.getPosts().size.toLong() + 1L,
           topicId = topicId,
           postContent = content,
           postCreatedAt = System.currentTimeMillis()
           )
       AppRepository.addPost(post)

    }

    fun getAvatarId(avatarId: Long): Int {
        return AppRepository.getAvatar(avatarId)
    }


}