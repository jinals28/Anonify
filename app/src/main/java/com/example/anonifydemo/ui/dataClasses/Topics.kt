package com.example.anonifydemo.ui.dataClasses

data class Topics(
    val id: Int,
    val name: String,
    var isSelected: Boolean = false,
    var priority: Int = 0
)
