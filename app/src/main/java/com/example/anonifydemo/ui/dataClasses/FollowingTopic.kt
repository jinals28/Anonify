package com.example.anonifydemo.ui.dataClasses

import java.util.Date

data class FollowingTopic(
    val followingTopicId: Long, // Room: Long (Primary key)
    val userId: Long, // Room: Long (Foreign key)
    val topicId: Long, // Room: Long (Foreign key)
    val followedAt: Long // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)
