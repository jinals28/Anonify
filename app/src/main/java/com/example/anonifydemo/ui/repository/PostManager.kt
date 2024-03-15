package com.example.anonifydemo.ui.repository

import android.util.Log
import com.example.anonifydemo.ui.dataClasses.Post

object PostManager {

    private val postList = mutableListOf<Post>()

    fun getInstance(): PostManager {
        return INSTANCE
    }

    private const val TAG = "PostManager"
    private val INSTANCE = PostManager

    fun addPost(post: Post) {
        Log.d("Anonify: Post Manager", post.toString())
        postList.add(post)
    }

    fun getPostList(): List<Post> {
        return postList
    }
}
