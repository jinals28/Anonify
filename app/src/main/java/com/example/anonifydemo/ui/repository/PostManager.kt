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
        Log.d("Anonify: com.example.anonifydemo.ui.dataClasses.Post Manager", post.toString())
        postList.add(post)
    }

    fun getPostList(): List<Post> {
        return postList
    }

//    fun updatePost(updatedPost: Post) {
//        val index = postList.indexOfFirst { it.uid == updatedPost.uid }
//        if (index != -1) {
//            postList[index] = updatedPost
//        }
//    }
//
//    fun getPostById(postId: Long): Post? {
//        return getPostList().find { it.postId == postId }
//    }
}
