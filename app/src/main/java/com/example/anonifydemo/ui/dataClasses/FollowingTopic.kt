package com.example.anonifydemo.ui.dataClasses

import java.util.Date

data class FollowingTopic(// Room: Long (Foreign key)
    val topic : String = "", // Room: Long (Foreign key)
    val followedAt: Long // Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)
