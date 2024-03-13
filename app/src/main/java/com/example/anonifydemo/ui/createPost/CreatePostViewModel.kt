package com.example.anonifydemo.ui.createPost

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreatePostViewModel : ViewModel() {

    private var topics : List<String> = listOf()

    private val _topicList = MutableLiveData<List<String>>()

    val topicList : LiveData<List<String>> = _topicList

    fun set(topicList : List<String>){

        topics = topicList
    }

    fun generateSuggestions(input : String){
        Log.d("Post View Model", input)
            _topicList.value = topics.filter { it.startsWith("$input", ignoreCase = true)}.toList()
            Log.d("Post View Model", _topicList.value.toString())
        }



}