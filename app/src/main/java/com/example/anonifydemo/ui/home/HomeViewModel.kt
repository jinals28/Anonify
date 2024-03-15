package com.example.anonifydemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.repository.PostManager

class HomeViewModel : ViewModel() {

    private val postManager = PostManager.getInstance()

    private val _postList = MutableLiveData<List<Post>>()
    val postList: LiveData<List<Post>> = _postList

    init {

        _postList.value = postManager.getPostList()
    }
}