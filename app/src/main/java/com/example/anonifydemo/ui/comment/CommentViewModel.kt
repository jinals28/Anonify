package com.example.anonifydemo.ui.comment

import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.repository.PostManager

class CommentViewModel : ViewModel() {

    private val postManager = PostManager.getInstance()

    fun getPostById(postId: Long): Post? {
        return postManager.getPostList().find { it.postId == postId }
    }
}