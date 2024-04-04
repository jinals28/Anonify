package com.example.anonifydemo.ui.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anonifydemo.R
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.dataClasses.Comment
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.dataClasses.Like
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.dataClasses.User
import com.example.anonifydemo.ui.utils.Utils
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object AppRepository : Utils {
    // Users table
// Pos
//    ts table
    private val posts = mutableListOf<Post>()

    // Comments table
    private val comments = mutableListOf<Comment>()

    val likeList = mutableListOf<Like>()

    const val TAG = "Anonify: USER_REPOSITORY"

    private val users = Firebase.firestore.collection("users")

    private val postsList = Firebase.firestore.collection("posts")

    private val followingTopics = Firebase.firestore.collection("followingTopics")

    private val avatarsCollection = Firebase.firestore.collection("avatars")

    private val topicCollection = Firebase.firestore.collection("topics")

    private var _topicList = MutableLiveData<List<Topic>>()

    val topicsList: LiveData<List<Topic>> = _topicList

//    var topicList: List<Topic> = mutableListOf()

    private val _postList = MutableLiveData<List<DisplayPost>>()

    val postList : LiveData<List<DisplayPost>> = _postList

    //TODO: Figure out what to do about topics list, should it be stored permannetly in the app like this or should it be fetched for the first time and then stored in app always.
    //Topics table
    var topicList = mutableListOf(
        Topic("#Entertainment"),
        Topic("#Social"),
        Topic("#FashionAndBeauty"),
        Topic("#Mentalhealth"),
        Topic("#InternshipAndJobs"),
        Topic("#CollegeStories"),
        Topic("#TravelTopics"),
        Topic("#Technology"),
        Topic("#Career"),
        Topic("#Anxiety"),
        Topic("#Family"),
        Topic("#Sports"),
        Topic("#Addiction"),
        Topic("#Advice"),
        Topic("#Aging"),
        Topic("#BadHabits"),
        Topic("#BodyShaming"),
        Topic("#TimeManagement"),
        Topic("#Racism"),
        Topic("#Other")
    )

    val avatarList = listOf(
        Avatar(R.drawable.dinosaur,"Moki"),
        Avatar(R.drawable.dog,"Jinto"),
        Avatar(R.drawable.panda,"Yarri"),
        Avatar(R.drawable.rabbit,"Zink"),
        Avatar(R.drawable.bear,"Loki"),
        Avatar(R.drawable.cat,"Yolo"),
        Avatar(R.drawable.octopus,"Kairo"),
        Avatar(R.drawable.owl,"Lumi"),
        Avatar(R.drawable.deer,"Yara"),
        Avatar(R.drawable.tiger,"Lokai"),
        Avatar(R.drawable.shark,"Soli"),
        Avatar(R.drawable.elephant,"Juno"),
        Avatar(R.drawable.lion,"Simba"),
        Avatar(R.drawable.wolf,"Jinx"),
        Avatar(R.drawable.sloth,"Zinna"),
        Avatar(R.drawable.rabbit2,"Lexa"),
        Avatar(R.drawable.llama,"Lyric"),
        Avatar(R.drawable.penguin,"Zolar")
    )

    var followingTopicList = mutableListOf<FollowingTopic>()

    // Likes table
    private val likes = mutableListOf<Like>()

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

    suspend fun getTopics() {
        try {
            val list = mutableListOf<Topic>()
            val querySnapshot = topicCollection.get().await()
            for (document in querySnapshot.documents) {
                val name = document.getString("name") ?: ""
                // Assuming you store the resource ID as a string in Firestor
                list.add(Topic(name))
                Log.d(TAG, list.toString())
            }
            topicList = list
            _topicList.value = list
        } catch (e: Exception) {
            Log.d(TAG, "eXCEPTION: $e.localizedMessage!!")
        }

    }

    suspend fun getFollowingTopicsForUser(userId: String): MutableList<FollowingTopic> {
        Log.d(TAG, "Repo getFollowing")
        var followingTopics = mutableListOf<FollowingTopic>()
        try{
        val docRef = Firebase.firestore.collection("followingTopics").document(userId)
        val doc = docRef.get().await()
        if (doc.exists()) {
            val data = doc.data
            if (data != null) {
                for ((topic, topicData) in data) {
                    if (topicData is Map<*, *>) {
                        val followedAt = topicData["followedAt"] as? Long
                        if (followedAt != null) {
                            followingTopics.add(
                                FollowingTopic(
                                    topic = topic,
                                    followedAt = followedAt
                                )
                            )
                        }else{
                            // Handle missing or invalid followedAt data
                            Log.e(
                                TAG,
                                "Invalid followedAt data for topic: $topic"
                            )
                        }
                    }else {
                        // Handle unexpected topic data format
                        Log.e(
                            TAG,
                            "Unexpected data format for topic: $topic"
                        )
                    }
                }
            }else {
                // Handle null data
                Log.e(TAG, "Null data received for user: $userId")
            }
            } else {
            // Handle non-existent document
            Log.e(TAG, "No following topics found for user: $userId")
        }
        } catch (e: Exception)
    {
        // Handle exceptions
        Log.e(TAG, "Error fetching following topics for user: $userId", e)
    }
        Log.d(TAG, followingTopics.toString())
        followingTopicList = followingTopics
    return followingTopics
}

    suspend fun getUserByUid(uid: String): Pair<User, List<FollowingTopic>> {
        return withContext(Dispatchers.IO) {
            val documentSnapshot = users.document(uid).get().await()
            val user = documentSnapshot.toObject(User::class.java)
            log("AppRepo ${user.toString()} ")
            val followingTopicList = getFollowingTopicsForUser(user!!.uid)
            log(followingTopicList.toString())
            Pair(user, followingTopicList) ?: throw IllegalStateException("User not found")
        }
    }

    suspend fun saveSelectedTopics(selectedTopics: List<FollowingTopic>, userId: String) {
        val userDocRef = followingTopics.document(userId)

        // Create a map of topics with their followed timestamps
        val topicsMap = selectedTopics.associateBy { it.topic } // Assuming topic is unique

        // Save the map to Firestore
        userDocRef.set(topicsMap)
    }

    private fun User.toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "createdAt" to createdAt
        )
    }

    suspend fun addTopics(topicsList: List<Topic>) {
        try {
//            for (topic in topicsList) {
//                topics
//                    .document(topic.name)
//                    .set(mapOf("name" to topic.name))
//                    .addOnSuccessListener { documentReference ->
//                        Log.d(TAG, "DocumentSnapshot added with ID:")
//
//                    }
//                    .addOnCanceledListener {
//                        Log.d(TAG, "cancelled ")
//                    }
//                    .addOnFailureListener { e ->
//                        Log.d(TAG, "DocumentSnapshot added with ID: ${e.localizedMessage}")
//                    }
//            }

        } catch (e: Exception) {
            println("Error adding topics to Firestore: ${e.localizedMessage}")
        }
    }
    suspend fun updateUserAvatar(userId: String, avatar: String) {
            try {
                // Create a map with the avatar details to update in the user document
//                val avatarMap = mapOf(
//                    "name" to avatar,
//                )

                // Update the avatar field in the user document
                users.document(userId)
                    .update("avatar", avatar)
                    .await()
            } catch (e: Exception) {
                // Handle any errors
            }
        }

    suspend fun fetchPosts(followingTopicsList: List<FollowingTopic>) {
        log("In Fetch Posts")
            val posts = mutableListOf<DisplayPost>()

        val followingTopic = followingTopicsList.map { it.topic }
            try {
                val querySnapshot: QuerySnapshot = postsList.whereIn("topicName", followingTopic).orderBy("postCreatedAt", Query.Direction.DESCENDING).get().await()
                for (document in querySnapshot.documents) {
                    log("App REpo: Post Id: ${document.id}")
                    val userId = document.getString("userId") ?: ""
                    val topicName = document.getString("topicName") ?: ""
                    val postContent = document.getString("postContent") ?: ""
                    val postCreatedAt = document.getLong("postCreatedAt") ?: -1L
                    val likeCount = document.getLong("likeCount") ?: -1L
                    val avatarName = fetchAvatarName(userId)
                    log("Avatar name from userId: $avatarName")
                    val url = avatarList.find { it.name == avatarName }!!.url

                    val post = DisplayPost(
                        postId = document.id,
                        postContent = postContent,
                        topicName = topicName,
                        likeCount = likeCount.toInt(),
                        avatarName = avatarName,
                        avatarUrl = url,
                    )
                    posts.add(post)
                    log("App Repo, ${posts.toString()}")
                }
                _postList.value = posts
            } catch (e: Exception) {
                // Handle exceptions, such as Firestore errors or parsing errors
               log(e.message.toString())
            }
        }

    private suspend fun fetchAvatarName(userId: String): String{
        try {
            log("App Repo: fetchAvatarName : In fun")
            val documentSnapshot = users.document(userId).get().await()
            return documentSnapshot.getString("avatar") ?: ""
        } catch (e: Exception) {
            // Handle exceptions, such as Firestore errors or parsing errors
          log(e.localizedMessage)
        }
        return ""
    }

    fun getAvatar(avatarName : String): Avatar {

        return avatarList.find { it.name == avatarName }!!
    }

    suspend fun addPost(post: Post) {
            Log.d("Anonify : Post", posts.toString())
            try {
                val postCollection = Firebase.firestore.collection("posts")
                postCollection.add(post)
                    .addOnSuccessListener { documentReference ->
                        Log.d("AppRepository", "Post added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("AppRepository", "Error adding post", e)
                    }
            } catch (e: Exception) {
                Log.e("AppRepository", "Exception adding post", e)
            }
        }

        // Add comment to repository
        fun addComment(comment: Comment) {
            comments.add(comment)
        }

        // Add topic to repository
        fun addTopic(topic: Topic) {
//            topics.add(topic)
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

        fun getFollowingTopics(): List<FollowingTopic> {
            return followingTopicList
        }

        // Get all likes from repository
        fun getLikes(): List<Like> {
            return likes
        }

        fun getUser(uid: String): User? {

            return null

        }

        fun getHashtagId(hashtag: String): Long {

            return -1L

        }

        fun getPostsForUser(userId: Long): List<Post> {

//            val list = followingTopicList.filter { it.userId == userId }.map {
//                it.topicId
//            }
//            Log.d("Anonify: Repo", list.toString())
//            Log.d("Anonify: Repo", "Followinglist" + followingTopicList.toString())
//            return getPosts().filter { post ->
//                list.contains(post.topicId)
//            }.sortedByDescending { post ->
//                post.postCreatedAt
//            }
            return listOf()
        }

        fun getAvatarOb(avatarId: Long): Avatar {
//        return avatarList.find { it.avatarId == avatarId }!!
            return Avatar()
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
            } catch (e: Exception) {
                Log.d("Anonify : App Repo", "Exception ${e.message}toString() ")

            }
            return mutableListOf()

        }

        fun getTopicById(topicId: Long): Topic {
            return Topic("")
        }

        fun updateUser(updatedUser: User) {
            try {
//        val existingIndex = users.indexOfFirst { it.userId == updatedUser.userId }
//        if (existingIndex != -1) {
//            users.removeAt(existingIndex)
//            users.add(existingIndex, updatedUser)
//        }else{
//            Log.d("Anonfy repo", "-1 found")
//        }
            } catch (e: Exception) {
                Log.d("Anonify Repo", e.toString())
            }

        }
        // Other methods for fetching data can be added similarly
    }


//    suspend fun getAvatars() {
//        val avatars = MutableLiveData<List<Avatar>>()
//        try {
//            val list = mutableListOf<Avatar>()
//            val querySnapshot = avatarsCollection.get().await()
//            for (document in querySnapshot.documents) {
//                val name = document.getString("name") ?: ""
//                // Assuming you store the resource ID as a string in Firestore
//                val avatarId = document.getLong("url") ?: -1L
//
//                list.add(Avatar(avatarId.toInt(), name))
//                Log.d(TAG, list.toString())
//            }
//            _avatarList.value = list
//        } catch (e: Exception) {
//            Log.d(TAG, "eXCEPTION: $e.localizedMessage!!")
//        }
//
//    }

//        fun getFollowingTopicList(userId: String): List<FollowingTopic> {
//
//            return emptyList()
//        }

//    fun getFollowingTopicIdListForUser(userId: Long): List<Long> {
//        return
//    }


//    private fun getUserById(userId: Long): User {
//        Log.d("Anonify : Repo.getUserByID", getUsers().toString())
//        return getUsers().find { it.userId == userId }!!
//    }

//val followingTopicList  = doc.toObject(FollowingTopic::class.java)
//        .addOnSuccessListener { documentSnapshot ->
//            if (documentSnapshot.exists()) {
//
//                for (doc in documentSnapshot.data!!.entries) {
//                    val topic = doc.key
//                    val followedData = doc.value as Map<*, *> // Assuming the value is a Map<String, Any>
//                    val followedAt = followedData["followedAt"] as Long
//                    followingTopics.add(FollowingTopic(topic = topic, followedAt = followedAt))
//
////                    val followedAt = doc.value. as Long
////                    followingTopics.add(FollowingTopic(topic = topic, followedAt = followedAt))
// Navigate to home fragment
//        .addOnFailureListener { e ->
//            // Handle failure
//            Log.e("SignInFragment", "Error fetching following topics: ${e.message}", e)
//            // Navigate to chooseAvatarFragment
//
//        }

// Get all topics from repository
//        fun getTopics(): List<Topic> {
//            return listOf()
//        }


//


//    val avatarList = listOf<Avatar>(
//        Avatar(R.drawable.dinosaur, "Moki"),
//        Avatar(R.drawable.dog, "Jinto"),
//        Avatar(R.drawable.panda, "Yarri"),
//        Avatar(R.drawable.rabbit, "Zink"),
//        Avatar(R.drawable.bear, "Loki"),
//        Avatar(R.drawable.cat, "Yolo"),
//        Avatar(R.drawable.octopus, "Kairo"),
//        Avatar(R.drawable.owl, "Lumi"),
//        Avatar(R.drawable.deer, "Yara"),
//        Avatar(R.drawable.tiger, "Lokai"),
//        Avatar(R.drawable.shark, "Soli"),
//        Avatar(R.drawable.elephant, "Juno"),
//        Avatar(R.drawable.lion, "Simba"),
//        Avatar(R.drawable.wolf, "Jinx"),
//        Avatar(R.drawable.sloth, "Zinna"),
//        Avatar(R.drawable.rabbit2, "Lexa"),
//        Avatar(R.drawable.llama, "Lyric"),
//        Avatar(R.drawable.penguin, "Zolar")
//    )

