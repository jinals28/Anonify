package com.example.anonifydemo.ui.createPost

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.repository.PostManager
import com.example.anonifydemo.ui.utils.ValidationUtil

class CreatePostViewModel : ViewModel() {

    private var topics: List<String> = listOf()

    private val _topicList = MutableLiveData<List<String>>()

    val topicList: LiveData<List<String>> = _topicList

    private val postManager = PostManager.getInstance()

    fun set(topicList: List<String>) {

        topics = topicList
    }

    fun generateSuggestions(input: String) {
        Log.d("com.example.anonifydemo.ui.dataClasses.Post View Model", input)
        _topicList.value = topics.filter { it.startsWith("$input", ignoreCase = true) }.toList()
        Log.d("com.example.anonifydemo.ui.dataClasses.Post View Model", _topicList.value.toString())
    }

    fun isValidContent(content: String): Boolean {
        return ValidationUtil.isValidPostContent(content)
    }

    fun isValidHashtag(hashtag: String): Boolean {
        // Implement hashtag validation logic
        // Check if the hashtag is in the predefined suggestion list
        return ValidationUtil.isHashtagValid(hashtag, topics) // Replace with your validation logic
    }

    fun addPost(post: Post) {

        postManager.addPost(post)

    }


}