package com.example.anonifydemo.ui.utils

import android.util.Log
import com.example.anonifydemo.ui.dataClasses.DisplayLike
import com.example.anonifydemo.ui.dataClasses.DisplayPost

interface Likeable {


    companion object {
        private const val TAG = "LIKEABLE"
    }


    fun initializeUserLikes(postList : List<DisplayPost>){
    }
    fun likePost(post: DisplayPost) {
//        post.likeCount++
//        post.likedByCurrentUser = true
//        setLikeButtonState(true)
//        setLikeCountText(post.likeCount)
//        userLikes.find { it.postId == post.postId }?.liked = true
    }

    fun unlikePost(post: DisplayPost) {
//        setLikeButtonState(false)
//        post.likeCount--
//        post.likedByCurrentUser = false
//        userLikes.find { it.postId == post.postId }?.apply { liked = false }
//        setLikeCountText(post.likeCount)
    }

    fun togglePost(post: DisplayPost) {
//        val isLiked = userLikes.find { it.postId == post.postId }?.liked
//        if (isLiked == true) {
//            unlikePost(post)
//        } else {
//            likePost(post)
//        }
//        debounceUpdateToDatabase(userLikes)
    }

    fun setLikeButtonState(isLiked: Boolean) {
        // Implementation specific to your RecyclerView adapter
    }

    fun setLikeCountText(likeCount: Int) {
        // Implementation specific to your RecyclerView adapter
    }

    fun debounceUpdateToDatabase(userLikes: MutableList<DisplayLike>) {
        // Implementation specific to your RecyclerView adapter
    }
}
