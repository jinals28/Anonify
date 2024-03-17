package com.example.anonifydemo.ui.dataClasses

data class Post(
    val postId : Long,
    val uid: String,
    val text: String,
    val hashtag: String,
    var likeCount: Int = 0,
    var commentCount: Int = 0,
    var saveCount: Int = 0,
    var advicePoint: Int = 0,
    val likedBy: MutableList<String> = mutableListOf(),
    val commentList : MutableList<Comment> = mutableListOf()// Store user IDs who liked the post
){
    fun addComment(comment: Comment) {
        commentList.add(comment)
        commentCount++
    }

}

