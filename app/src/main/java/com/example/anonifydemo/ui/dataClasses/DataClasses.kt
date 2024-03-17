package com.example.anonifydemo.ui.dataClasses

import java.util.*

//data class User(
//    val userId: Long, // Room: Long (Primary key)
//    val uid: String, // Firebase: String, Room: String
//    val name: String,
//    val email: String,
//    val avatarId: String, // Assuming this is a unique identifier for the avatar
//    val createdAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
//)

//data class Avatar(
//    val avatarId: String, // Assuming this is a unique identifier for the avatar
//    val url: String,
//    val name: String
//)

data class Topic(
    val topicId: Long, // Room: Long (Primary key)
    val name: String
)

data class FollowingTopic(
    val followingTopicId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val topicId: Long, // Room: Long (Foreign key)
    val followedAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

//data class Post(
//    val postId: Long, // Room: Long (Primary key)
//    val userId: Long, // Room: Long (Foreign key)
//    val topicId: Long, // Room: Long (Foreign key)
//    val postContent: String,
//    val postCreatedAt: Date // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
//)

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
