package com.example.anonifydemo.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.repository.PostManager

class CommentViewModel : ViewModel() {

    private val _post = MutableLiveData<DisplayPost>()

    val post : LiveData<DisplayPost> = _post
    suspend fun getPostById(postId: String){

        _post.value = AppRepository.fetchPost(postId)

    }

    fun postComment(commentText: String) {


    }

    private val postManager = PostManager.getInstance()

//    fun getPostById(postId: Long): Post? {
//        return postManager.getPostList().find { it.postId == postId }
//    }
}