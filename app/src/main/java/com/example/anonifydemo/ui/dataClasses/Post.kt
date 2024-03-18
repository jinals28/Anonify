package com.example.anonifydemo.ui.dataClasses

import java.util.Date

data class DisplayPost(
    val postId: Long,
    val postContent: String,
    val avatarUrl: Int, // Assuming this is the URL of the avatar image
    val avatarName: String,
    val topicName: String,
    val likeCount: Int,
    val commentCount: Int
)


data class Post(
    val postId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val topicId: Long, // Room: Long (Foreign key)
    val postContent: String,
    val postCreatedAt: Long // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

