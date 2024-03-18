package com.example.anonifydemo.ui.repository

import android.util.Log
import com.example.anonifydemo.R
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.dataClasses.Comment
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.dataClasses.Like
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.dataClasses.User
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object AppRepository {
    // Users table

    // Posts table
    private val posts = mutableListOf<Post>()

    // Comments table
    private val comments = mutableListOf<Comment>()

    const val TAG = "USER_REPOSITORY"

    val users = Firebase.firestore.collection("users")

    suspend fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        Log.d(TAG, "Add user")
        users
            .document(user.uid)
            .set(user.toMap())
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID:")
                onSuccess()
            }
            .addOnCanceledListener {
                Log.d(TAG, "cancelled ")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${e.localizedMessage}")
                onFailure(e)
            }
    }

    private fun User.toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "createdAt" to createdAt
        )
    }

    // Topics table
    private val topics = mutableListOf<Topic>(
        Topic(1, "#Entertainment"),
        Topic(2, "#Social"),
        Topic(3, "#FashionAndBeauty"),
        Topic(4, "#Mentalhealth"),
        Topic(5, "#InternshipAndJobs"),
        Topic(6, "#CollegeStories"),
        Topic(7, "#TravelTopics"),
        Topic(8, "#Technology"),
        Topic(9, "#Career"),
        Topic(10, "#Anxiety"),
        Topic(11, "#Family"),
        Topic(12, "#Sports"),
        Topic(13, "#Addiction"),
        Topic(14, "#Advice"),
        Topic(15, "#Aging"),
        Topic(16, "#BadHabits"),
        Topic(17, "#BodyShaming"),
        Topic(18, "#TimeManagement"),
        Topic(19, "#Racism"),
        Topic(20, "#Other")
    )

    private val followingTopicList = mutableListOf<FollowingTopic>()

    // Likes table
    private val likes = mutableListOf<Like>()

    private val avatarList = listOf<Avatar>(
        Avatar(1,R.drawable.dinosaur,"Moki"),
        Avatar(2,R.drawable.dog,"Jinto"),
        Avatar(3,R.drawable.panda,"Yarri"),
        Avatar(4,R.drawable.rabbit,"Zink"),
        Avatar(5,R.drawable.bear,"Loki"),
        Avatar(6,R.drawable.cat,"Yolo"),
        Avatar(7,R.drawable.octopus,"Kairo"),
        Avatar(8,R.drawable.owl,"Lumi"),
        Avatar(9,R.drawable.deer,"Yara"),
        Avatar(10,R.drawable.tiger,"Lokai"),
        Avatar(11,R.drawable.shark,"Soli"),
        Avatar(12,R.drawable.elephant,"Juno"),
        Avatar(13,R.drawable.lion,"Simba"),
        Avatar(14,R.drawable.wolf,"Jinx"),
        Avatar(15,R.drawable.sloth,"Zinna"),
        Avatar(16,R.drawable.rabbit2,"Lexa"),
        Avatar(17,R.drawable.llama,"Lyric"),
        Avatar(18,R.drawable.penguin,"Zolar")
    )

    // Other tables can be similarly defined

    // Add user to repository
    fun addUser(user: User) {
        users.add(user)
        Log.d("Anonify : Repo", users.toString())
    }

    // Add post to repository
    fun addPost(post: Post) {
        posts.add(post)
        Log.d("Anonify : Post", posts.toString())
    }

    // Add comment to repository
    fun addComment(comment: Comment) {
        comments.add(comment)
    }

    // Add topic to repository
    fun addTopic(topic: Topic) {
        topics.add(topic)
    }

    fun addFollowingTopic(followingTopic: FollowingTopic) {
        followingTopicList.add(followingTopic)
    }

    // Add like to repository
    fun addLike(like: Like) {
        likes.add(like)
    }

    // Other CRUD operations can be added similarly

    // Get all users from repository
    fun getUsers(): List<User> {
        return mutableListOf()
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

    fun getFollowingTopics(): List<FollowingTopic> {
        return followingTopicList
    }

    // Get all likes from repository
    fun getLikes(): List<Like> {
        return likes
    }

    fun getAvatarList() : List<Avatar>{
        return avatarList
    }

    fun getUser(uid: String): User? {

        return null

    }

    fun getAvatar(avatarId: Long): Int {
        return avatarList.find { it.avatarId == avatarId }!!.url
    }

    fun getHashtagId(hashtag: String): Long {

        return topics.find{ it.name == hashtag}!!.topicId

    }

    fun getFollowingTopicList(userId: Long): List<FollowingTopic> {

        return followingTopicList.filter { it.userId == userId }
    }

//    fun getFollowingTopicIdListForUser(userId: Long): List<Long> {
//        return
//    }

    fun getPostsForUser(userId: Long): List<Post> {

        val list = followingTopicList.filter { it.userId == userId }.map {
            it.topicId
        }
        Log.d("Anonify: Repo", list.toString())
        Log.d("Anonify: Repo", "Followinglist" + followingTopicList.toString())
        return getPosts().filter { post ->
            list.contains(post.topicId)
        }.sortedByDescending { post ->
            post.postCreatedAt
        }
    }

    private fun getAvatarOb(avatarId: Long): Avatar{
        return avatarList.find { it.avatarId == avatarId }!!
    }

    fun getDisplayPostsForUser(userId: Long): List<DisplayPost> {

        val userPosts = getPostsForUser(userId)
        Log.d("Anonify, Repo.DisplaYPOst", "${userPosts.toString()}")
        try {
//            val displayPostlist = userPosts.map { post ->
//                val user = getUserById(post.userId)
//                Log.d("Anonify : App Repo", "${user.toString()} ")
//                val avatarUrl = getAvatarOb(user.avatarId)
//                Log.d("Anonify : App Repo", "${avatarUrl.toString()} ")
//                val topicName = getTopicById(post.topicId).name
//                val likeCount = getLikes().count { it.postId == post.postId }
//                val commentCount = getComments().count { it.postId == post.postId }
//                DisplayPost(
//                    postId = post.postId,
//                    postContent = post.postContent,
//                    avatarUrl = avatarUrl.url,
//                    avatarName = avatarUrl.name, // Use any user identifier you want to display
//                    topicName = topicName,
//                    likeCount = likeCount,
//                    commentCount = commentCount
//                )

            return mutableListOf()
        }catch(e: Exception){
            Log.d("Anonify : App Repo", "Exception ${e.message}toString() ")

            }
        return mutableListOf()

        }


    private fun getTopicById(topicId: Long): Topic {
        return topics.find { it.topicId == topicId }!!
    }

//    private fun getUserById(userId: Long): User {
//        Log.d("Anonify : Repo.getUserByID", getUsers().toString())
//        return getUsers().find { it.userId == userId }!!
//    }

    fun updateUser(updatedUser: User) {
        try{
//        val existingIndex = users.indexOfFirst { it.userId == updatedUser.userId }
//        if (existingIndex != -1) {
//            users.removeAt(existingIndex)
//            users.add(existingIndex, updatedUser)
//        }else{
//            Log.d("Anonfy repo", "-1 found")
//        }
        }catch (e: Exception){
            Log.d("Anonify Repo", e.toString())
        }

    }

    suspend fun getUserByUid(uid: String): User? {
        return withContext(Dispatchers.IO) {
            val documentSnapshot = users.document(uid).get().await()
            val user = documentSnapshot.toObject(User::class.java)
            user ?: throw IllegalStateException("User not found")
        }
    }


    // Other methods for fetching data can be added similarly
}
