package com.example.anonifydemo.ui.dataClasses

data class User(
    var uid: String = "",
    var email: String? = null,
    val avatarUrl: Avatar = Avatar(0, ""),
    val topics: MutableList<Topics> = mutableListOf(),
    val createdAt: Long = System.currentTimeMillis()
)
