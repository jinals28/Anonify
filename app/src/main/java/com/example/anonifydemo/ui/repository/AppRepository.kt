package com.example.anonifydemo.ui.repository

import android.util.Log
import com.example.anonifydemo.ui.dataClasses.Comment
import com.example.anonifydemo.ui.dataClasses.Like
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.dataClasses.User

object AppRepository {
    // Users table
    private val users = mutableListOf<User>()

    // Posts table
    private val posts = mutableListOf<Post>()

    // Comments table
    private val comments = mutableListOf<Comment>()

    // Topics table
    private val topics = mutableListOf<Topic>()

    // Likes table
    private val likes = mutableListOf<Like>()

    // Other tables can be similarly defined

    // Add user to repository
    fun addUser(user: User) {
        users.add(user)
        Log.d("Anonify : Repo", users.toString())
    }

    // Add post to repository
    fun addPost(post: Post) {
        posts.add(post)
    }

    // Add comment to repository
    fun addComment(comment: Comment) {
        comments.add(comment)
    }

    // Add topic to repository
    fun addTopic(topic: Topic) {
        topics.add(topic)
    }

    // Add like to repository
    fun addLike(like: Like) {
        likes.add(like)
    }

    // Other CRUD operations can be added similarly

    // Get all users from repository
    fun getUsers(): List<User> {
        return users
    }

    // Get all posts from repository
    fun getPosts(): List<Post> {
        return posts
    }

    // Get all comments from repository
    fun getComments(): List<Comment> {
        return comments
    }

    // Get all topics from repository
    fun getTopics(): List<Topic> {
        return topics
    }

    // Get all likes from repository
    fun getLikes(): List<Like> {
        return likes
    }

    // Other methods for fetching data can be added similarly
}
