package com.example.anonifydemo.ui.dataClasses

//data class User(
//    var uid: String = "",
//    var email: String? = null,
//    val avatarUrl: Avatar = Avatar(0, ""),
//    val topics: MutableList<Topics> = mutableListOf(),
//    val createdAt: Long = System.currentTimeMillis()
//)

data class User(
//    val userId: Long = -1, // Room: Long (Primary key)
    val uid: String = "", // Firebase: String, Room: String
    val email: String = "",
    val avatar: String = "", // Assuming this is a unique identifier for the avatar
    val createdAt: Long = -1,
    var advicePoint : Long = 0// Firebase: Timestamp, Room: Long (timestamp converted to milliseconds)
)
