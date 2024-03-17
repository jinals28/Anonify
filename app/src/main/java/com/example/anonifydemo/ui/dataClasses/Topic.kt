package com.example.anonifydemo.ui.dataClasses

//data class Topics(
//    val id: Int,
//    val name: String,
//    var isSelected: Boolean = false,
//    var priority: Int = 0
//)
data class Topic(
    val topicId: Long, // Room: Long (Primary key)
    val name: String
)