package com.example.anonifydemo.ui.dataClasses

data class Post(
    val uid: String,
    val text: String,
    val hashtag: String,
    var likeCount: Int = 0,
    var commentCount: Int = 0,
    var saveCount: Int = 0,
    var advicePoint: Int = 0,
    val likedBy: MutableList<String> = mutableListOf() // Store user IDs who liked the post
)

