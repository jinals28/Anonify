package com.example.anonifydemo.ui.dataClasses

data class DisplayPost(
    val postId: String = "",
    val userId : String = "",
    val postContent: String,
    val avatarUrl: Int, // Assuming this is the URL of the avatar image
    val avatarName: String,
    val topicName: String,
    var likeCount: Long,
    val commentCount: Long = 0L,
    var likedByCurrentUser: Boolean,
    var isSavedByUser : Boolean
)

data class Post(// Room: Long (Primary key)
    val userId: String, // Room: Long (Foreign key)
    val topicName: String, // Room: Long (Foreign key)
    val postContent: String,
    val postCreatedAt: Long,
    val likeCount: Long = 0L,
    val commentCount: Long = 0L
    // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)
