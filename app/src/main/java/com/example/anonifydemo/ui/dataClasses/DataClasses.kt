package com.example.anonifydemo.ui.dataClasses

import java.util.*

data class Like(
    val likeId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val postId: Long, // Room: Long (Foreign key)
    val likedAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

data class Comment(
    val commentId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val postId: Long, // Room: Long (Foreign key)
    val commentText: String,
    val commentedAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

data class CommentLike(
    val commentLikesId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val commentId: Long, // Room: Long (Foreign key)
    val likedCommentAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

data class SavePost(
    val savePostId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val postId: Long, // Room: Long (Foreign key)
    val savedPostAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

data class ReportPost(
    val reportPostId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val postId: Long, // Room: Long (Foreign key)
    val reportedPostAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

data class ReportUser(
    val reportedUserId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val reportedAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

data class AdvicePoints(
    val advicePointId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val commentId: Long, // Room: Long (Foreign key)
    val advicePointAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)
