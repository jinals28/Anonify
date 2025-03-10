package com.example.anonifydemo.ui.dataClasses

import java.util.*

data class ActiveUser(
    val uid: String = "", // Firebase: String, Room: String
    val email: String = "",
    val avatar: Avatar = Avatar(), // Assuming this is a unique identifier for the avatar
    val createdAt: Long = -1,
    val followingTopics: List<FollowingTopic> = listOf(),
    var advicePointCount: Long = 0,
    var followingTopicsCount: Long = 0,
    var postCount: Long = 0,
    var bio : String = ""
)

data class DisplayCommunity(
    val communityName : String,
    val followerCount : Long = 0,
    val postCount : Long = 0,
    val isFollowedByUser : Boolean = false
)

data class Like(
    val likeId: String = "", // Room: Long (Primary key)
    val userId: String= "", // Room: Long (Foreign key)
    val postId: String, // Room: Long (Foreign key)
    val likedAt: Long // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)

data class DisplayLike(
    val postId: String,
    var likedAt: Long,
    var liked: Boolean // Indicator to show if it was liked or not
)

data class DisplaySaved(
    val postId: String,
    var savedAt: Long,
    var save: Boolean // Indicator to show if it was liked or not
)

data class DisplayCommentLike(
    val commentId: String,
    var likedAt: Long,
    var liked: Boolean // Indicator to show if it was liked or not
)

data class DisplayAdvicePoint(
    val commentId: String,
    var advicepPointGivenAt: Long,
    var given: Boolean // Indicator to show if it was liked or not
)

data class Comment(
    val userId: String, // Room: Long (Foreign key)
    val postId: String, // Room: Long (Foreign key)
    val commentText: String,
    val commentedAt: Long, // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
    val commentLikeCount : Long = 0,
    val advicePointCount: Long = 0,
    val reportedCount : Long = 0

)

data class DisplayComment(
    val userName: String,
    val userId : String,
    val avatarUrl: Int, // Assuming it's the resource ID of the avatar image
    val postContent: String,
    var likeCount: Long,
    val commentId: String,
    var likedByUser : Boolean = false,
    var advicePointByUser : Boolean = false,
    var advicePointCount : Long

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

