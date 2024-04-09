package com.example.anonifydemo.ui.repository

import com.google.firebase.auth.FirebaseAuth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.anonifydemo.R
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.dataClasses.Comment
import com.example.anonifydemo.ui.dataClasses.DisplayAdvicePoint
import com.example.anonifydemo.ui.dataClasses.DisplayComment
import com.example.anonifydemo.ui.dataClasses.DisplayCommentLike
import com.example.anonifydemo.ui.dataClasses.DisplayLike
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.DisplaySaved
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.dataClasses.User
import com.example.anonifydemo.ui.utils.Utils
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object AppRepository : Utils {

    val firestore = Firebase.firestore

    private val posts = mutableListOf<Post>()

    const val TAG = "Anonify: USER_REPOSITORY"

    private val _hidePostList = mutableListOf<String>()

    private val _hideCommentList = mutableListOf<String>()

    private val _commentsLiveData = MutableLiveData<List<DisplayComment>>()
    val commentsLiveData: LiveData<List<DisplayComment>> get() = _commentsLiveData

    private val users = Firebase.firestore.collection("users")

    private val postsList = Firebase.firestore.collection("posts")

    private val followingTopics = Firebase.firestore.collection("followingTopics")

    private val avatarsCollection = Firebase.firestore.collection("avatars")

    private val topicCollection = Firebase.firestore.collection("topics")

    private val likesCollection = Firebase.firestore.collection("likes")

    private val commentCollection = Firebase.firestore.collection("comments")

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



    suspend fun updateUserAvatar(userId: String, avatar: String) {
            try {

                // Update the avatar field in the user document
                users.document(userId)
                    .update("avatar", avatar)
                    .await()
            } catch (e: Exception) {
                // Handle any errors
            }
        }

    private suspend fun isPostLikedByCurrentUser(postId: String, userId: String): Boolean {

        val commentLikeRef = postsList
            .document(postId)
            .collection("likes")
            .document(userId)

        return try {
            val documentSnapshot = commentLikeRef.get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            // Handle exceptions, such as Firestore errors
            false
        }
    }

    suspend fun fetchPosts(userId: String, followingTopicsList: List<FollowingTopic>) {
//        log("In Fetch Posts")
            val posts = mutableListOf<DisplayPost>()
        _hidePostList.clear()
        val userRef = users.document(userId).collection("reportedPosts").get().await()
        for (doc in userRef){
            _hidePostList.add(doc.id)
        }

        val followingTopic = followingTopicsList.map { it.topic }
        log("followingTopics ${followingTopic.toString()}")
            try {
                val querySnapshot: QuerySnapshot = postsList.whereIn("topicName", followingTopic).orderBy("postCreatedAt", Query.Direction.DESCENDING).get().await()
                for (document in querySnapshot.documents) {
//                    log("App REpo: Post Id: ${document.id}")
                    if (!_hidePostList.contains(document.id)) {

                        val postedUserId = document.getString("userId") ?: ""
                        val topicName = document.getString("topicName") ?: ""
                        val postContent = document.getString("postContent") ?: ""
                        val postCreatedAt = document.getLong("postCreatedAt") ?: -1L
                        val likeCount = document.getLong("likeCount") ?: 0L
                        val commentCount = document.getLong("commentCount") ?: 0L
                        val avatarName = fetchAvatarName(postedUserId)
//                    log("Avatar name from userId: $avatarName")
                        val url = avatarList.find { it.name == avatarName }!!.url

                        val likedByUser = isPostLikedByCurrentUser(postId = document.id, userId = userId)

                        val savedByUser = isSavedByUser(postId = document.id, userId = userId)

                        val post = DisplayPost(
                            postId = document.id,
                            userId = postedUserId,
                            postContent = postContent,
                            topicName = topicName,
                            likeCount = likeCount,
                            avatarName = avatarName,
                            avatarUrl = url,
                            likedByCurrentUser = likedByUser,
                            commentCount = commentCount,
                            isSavedByUser = savedByUser
                        )
                        posts.add(post)
//                    log("App Repo, ${posts.toString()}")
                    }
                }
                _postList.value = posts
            } catch (e: Exception) {
                // Handle exceptions, such as Firestore errors or parsing errors
               log(e.message.toString())
            }
        }

    private suspend fun fetchAvatarName(userId: String): String{
        try {
//            log("App Repo: fetchAvatarName : In fun")
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
                try {
                    // Get a reference to the posts collection


                    // Add the post to the posts collection
                    val postDocRef = postsList.add(post).await()
                    val postId = postDocRef.id

                    // Get a reference to the user's document
                    val userRef = users.document(post.userId)

                    // Create a reference to the user's posts subcollection
                    val userPostsCollection = userRef.collection("posts")

                    // Add the postId to the user's posts subcollection
                    val postData = hashMapOf(
                        "postId" to postId,
                        "postedAt" to post.postCreatedAt
                    )
                    userPostsCollection.document(postId).set(postData)

                    // Increment postCount for the user
                    userRef.update("postCount", FieldValue.increment(1))
                        .addOnSuccessListener {
                            Log.d("AppRepository", "Post added successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e("AppRepository", "Error updating postCount", e)
                        }
                } catch (e: Exception) {
                    Log.e("AppRepository", "Exception adding post", e)
                }
            }

    suspend fun updatesLikes(userId: String, userLikes: MutableList<DisplayLike>) {

        val batch = firestore.batch()

        userLikes.forEach { like ->
            val commentRef = postsList
                .document(like.postId)

            val likeRef = commentRef
                .collection("likes")
                .document(userId)

            if (like.liked) {
                // Liked, add or update the like
                val likeData = hashMapOf(
                    "likedAt" to like.likedAt,
                    "userId" to userId
                )
                batch.set(likeRef, likeData)
                batch.update(commentRef, "likeCount", FieldValue.increment(1))
            } else {
                // Unliked, remove the like
                batch.delete(likeRef)
                batch.update(commentRef, "likeCount", FieldValue.increment(-1))
            }
        }

        // Commit the batch operation
        batch.commit()
            .addOnSuccessListener {
                // Success

            }
            .addOnFailureListener { e ->
                // Handle failure
            }

    }

    //For Comment Fragment
    suspend fun fetchPost(userId : String, postId: String): DisplayPost? {

        try {
            fetchComments(userId, postId)
            val document: DocumentSnapshot = postsList.document(postId).get().await()

                val postedUserId = document.getString("userId") ?: ""
                val topicName = document.getString("topicName") ?: ""
                val postContent = document.getString("postContent") ?: ""
                val likeCount = document.getLong("likeCount") ?: 0L
                val commentCount = document.getLong("commentCount") ?: 0L
                val avatarName = fetchAvatarName(postedUserId)

                val url = avatarList.find { it.name == avatarName }!!.url

                val likedByUser = isPostLikedByCurrentUser(document.id, userId)

                val isSavedByUser = isSavedByUser(postId = document.id, userId = userId)

                val post = DisplayPost(
                    postId = document.id,
                    userId = postedUserId,
                    postContent = postContent,
                    topicName = topicName,
                    likeCount = likeCount,
                    avatarName = avatarName,
                    avatarUrl = url,
                    likedByCurrentUser = likedByUser,
                    commentCount = commentCount,
                    isSavedByUser = isSavedByUser
                )
                return post

            } catch (e: Exception) {
            // Handle exceptions, such as Firestore errors or parsing errors
            log(e.message.toString())
        }
        return null
    }

    suspend fun addCommentToPost(userId: String, comment: Comment, showSnackbar: (String) -> Unit, onNext: (List<DisplayComment>) -> Unit) {

        try {
            // Add comment to Comment Collection and obtain the generated document ID
            val documentReference = commentCollection.add(comment).await()
            val commentId = documentReference.id

            // Update the comment object with the generated commentI

            // Add comment reference to Post's comments collection
            val postRef = postsList.document(comment.postId)
            val commentsRef = postRef.collection("comments")

            // Add comment reference to Post's comments collection
            val commentMap = hashMapOf(
                "commentId" to commentId,
                "commentedAt" to System.currentTimeMillis() // Or use the commentedAt from the comment object
            )

            try {
                // Try to add comment reference to Post's comments collection
                commentsRef.document(commentId)
                    .set(commentMap, SetOptions.merge())
                    .await()

                postRef.update("commentCount", FieldValue.increment(1)).await()
                val commentList = fetchComments(userId, comment.postId)
                onNext(commentList)
                // Show success Snackbar
                showSnackbar("Comment added successfully")


            } catch (e: Exception) {
                // Handle errors specific to adding comment reference to Post's comments collection
                showSnackbar("Failed to add comment reference to Post's comments collection: ${e.message}")
                // Rollback by deleting the comment from Comment Collection
                commentCollection.document(commentId).delete().await()
            }

        } catch (e: Exception) {
            // Handle errors specific to adding comment to Comment Collection
            showSnackbar("Failed to add comment to Comment Collection: ${e.message}")
        }
    }

    suspend fun fetchComments(userId: String, postId: String) : List<DisplayComment> {

        val commentList = mutableListOf<DisplayComment>()

        val commentCollection = Firebase.firestore.collection("posts/$postId/comments")

        val deferred = CompletableDeferred<List<String>>()

        val userRef = users.document(userId).collection("reportedComments")



        try {
            _hideCommentList.clear()
            val reportedCommentsId = userRef.get().await()
            for (doc in reportedCommentsId){
                _hideCommentList.add(doc.id)
            }
            val snapshot = commentCollection.orderBy("commentedAt", Query.Direction.DESCENDING).get().await()
            for (document in snapshot.documents) {
                val commentId = document.id
                if (!_hideCommentList.contains(commentId)) {
                    val comment = fetchCommentObject(commentId)
                    if (comment != null) {
                        commentList.add(comment)
                    }
                }
            }
        } catch (e: Exception) {
            log(e.message.toString())
        }

        return commentList
    }

    //TODO: ONLY FETCH NEW COMMENTS BY USING TIME RANGE OF LAST COMMENT IN PREVIOUS LIST AND CURRENT TIME
    
    private suspend fun fetchCommentObject(commentId: String) : DisplayComment? {
        val doc = commentCollection.document(commentId)

        try {

                val document = doc.get().await()
                if (document.exists()) {
                    val userId = document.getString("userId") ?: ""
                    val commentText = document.getString("commentText") ?: ""
                    val commentLikeCount = document.getLong("likeCount") ?: 0L
                    val advicePointCount = document.getLong("advicePointCount") ?: 0L
                    val userName = fetchAvatarName(userId)
                    val url = avatarList.find { it.name == userName }!!.url

//                    val likedByUser = isPostLikedByCurrentUser(document.id, userId)

                    val likedByUser = isCommentLikedByUser(userId, commentId)
                    val advicePointByUser = isAdvicePointByUser(userId, commentId)
                    val comment = DisplayComment(
                        userName = userName,
                        userId = userId,
                        avatarUrl = url,
                        postContent = commentText,
                        likeCount = commentLikeCount,
                        commentId = commentId,
                        likedByUser = likedByUser,
                        advicePointByUser = advicePointByUser,
                        advicePointCount = advicePointCount
                    )
                    log(TAG + " Comment : " +comment.toString())
                    return comment
                }

        }catch (e : Exception){
            e.localizedMessage?.let { log(it) }
        }
        return null
    }

    fun updateCommentLikes(userId: String, userLikes: MutableList<DisplayCommentLike>) {
        val batch = Firebase.firestore.batch()

        userLikes.forEach { like ->
            val commentRef = firestore.collection("comments")
                .document(like.commentId)

                val commentLikeRef = commentRef
                .collection("commentLikes")
                .document(userId)

            if (like.liked) {
                // Liked, add or update the like
                val likeData = hashMapOf(
                    "commentLikedAt" to like.likedAt,
                    "userId" to userId
                )
                batch.set(commentLikeRef, likeData)
                batch.update(commentRef, "likeCount", FieldValue.increment(1))
            } else {
                // Unliked, remove the like
                batch.delete(commentLikeRef)
                batch.update(commentRef, "likeCount", FieldValue.increment(-1))
            }
        }

        // Commit the batch operation
        batch.commit()
            .addOnSuccessListener {
                // Success

            }
            .addOnFailureListener { e ->
                // Handle failure
            }


    }

    fun updateAdvicePoints(userId: String, userLikes: MutableList<DisplayAdvicePoint>) {
        val batch = Firebase.firestore.batch()

        userLikes.forEach { like ->
            val commentRef = firestore.collection("comments")
                .document(like.commentId)

            val commentLikeRef = commentRef
                .collection("advicePoints")
                .document(userId)

            val userRef = users.document(userId)

            if (like.given) {
                // Liked, add or update the like
                val likeData = hashMapOf(
                    "advicePointGivenAt" to like.advicepPointGivenAt,
                    "userId" to userId
                )
                batch.set(commentLikeRef, likeData)
                batch.update(commentRef, "advicePointCount", FieldValue.increment(1))
                batch.update(userRef, "advicePoint", FieldValue.increment(1))
            } else {
                // Unliked, remove the like
                batch.delete(commentLikeRef)
                batch.update(commentRef, "advicePointCount", FieldValue.increment(-1))
                batch.update(userRef, "advicePoint", FieldValue.increment(-1))
            }
        }

        // Commit the batch operation
        batch.commit()
            .addOnSuccessListener {
                // Success

            }
            .addOnFailureListener { e ->
                // Handle failure
            }


    }
    suspend fun isCommentLikedByUser(userId: String, commentId: String): Boolean {
        val commentLikeRef = commentCollection
            .document(commentId)
            .collection("commentLikes")
            .document(userId)

        return try {
            val documentSnapshot = commentLikeRef.get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            // Handle exceptions, such as Firestore errors
            false
        }
    }

    suspend fun isAdvicePointByUser(userId: String, commentId: String): Boolean {
        val commentLikeRef = commentCollection
            .document(commentId)
            .collection("advicePoints")
            .document(userId)

        return try {
            val documentSnapshot = commentLikeRef.get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            // Handle exceptions, such as Firestore errors
            false
        }
    }

    suspend fun isSavedByUser(userId: String, postId: String): Boolean {
        val savedPostRef = users
            .document(userId)
            .collection("savedPosts")
            .document(postId)

        return try {
            val documentSnapshot = savedPostRef.get().await()
            documentSnapshot.exists()
        } catch (e: Exception) {
            // Handle exceptions, such as Firestore errors
            false
        }
    }

    fun updateSavedPosts(userId: String, userLikes: MutableList<DisplaySaved>) {
        val batch = Firebase.firestore.batch()

        userLikes.forEach { like ->
            val commentRef = users
                .document(userId)

            val commentLikeRef = commentRef
                .collection("savedPosts")
                .document(like.postId)

            val userRef = users.document(userId)

            if (like.save) {
                // Liked, add or update the like
                val likeData = hashMapOf(
                    "SavedAt" to like.savedAt,
                    "postId" to like.postId
                )
                batch.set(commentLikeRef, likeData)
//                batch.update(commentRef, "advicePointCount", FieldValue.increment(1))
//                batch.update(userRef, "advicePoint", FieldValue.increment(1))
            } else {
                // Unliked, remove the like
                batch.delete(commentLikeRef)
//                batch.update(commentRef, "advicePointCount", FieldValue.increment(-1))
//                batch.update(userRef, "advicePoint", FieldValue.increment(-1))
            }
        }

        // Commit the batch operation
        batch.commit()
            .addOnSuccessListener {
                // Success

            }
            .addOnFailureListener { e ->
                // Handle failure
            }


    }

    suspend fun reportPost(coroutineScope: CoroutineScope,userId: String,  postUserId: String, postId: String) {
        try {
            var updatedReportedCount = 0L
            // Get the post document reference
            val postRef = postsList.document(postId)

            val userRef = users.document(userId)

            // Update reported count by 1 using Firestore transaction
            Firebase.firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val currentReportedCount = snapshot.getLong("reportedCount") ?: 0
                updatedReportedCount = currentReportedCount + 1
                transaction.update(postRef, "reportedCount", updatedReportedCount)
                if (updatedReportedCount >= 5) {
                    coroutineScope.launch {
                        increaseReportCount(postUserId)
                    }
                    transaction.delete(postRef)

                }
                val userPostsCollection = userRef.collection("reportedPosts")

                // Add the postId to the user's posts subcollection
                val reportedPost = hashMapOf(
                    "postId" to postId,
                    "reportedAt" to System.currentTimeMillis()
                )
                userPostsCollection.document(postId).set(reportedPost)
            }.addOnSuccessListener {
                Log.d(TAG, "Report count updated for post: $postId")
                // Check if reported count is 5, and if so, permanently delete the post

            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to update report count for post: $postId, ${e.message}", e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reporting post: $postId, ${e.message}", e)
        }
    }

    suspend fun reportComment(coroutineScope: CoroutineScope, userId : String,  commentOfUserId: String, commentId: String) {
        try {
            var updatedReportedCount = 0L
            // Get the post document reference
            val postRef = commentCollection.document(commentId)

            val userRef = users.document(userId)

            // Update reported count by 1 using Firestore transaction
            Firebase.firestore.runTransaction { transaction ->
                val snapshot = transaction.get(postRef)
                val currentReportedCount = snapshot.getLong("reportedCount") ?: 0
                updatedReportedCount = currentReportedCount + 1
                transaction.update(postRef, "reportedCount", updatedReportedCount)
                if (updatedReportedCount >= 5) {
                    coroutineScope.launch {
                        increaseReportCount(commentOfUserId)
                    }
                    transaction.delete(postRef)
                }
                val userPostsCollection = userRef.collection("reportedComments")

                // Add the postId to the user's posts subcollection
                val reportedComment = hashMapOf(
                    "commentId" to commentId,
                    "reportedAt" to System.currentTimeMillis()
                )
                userPostsCollection.document(commentId).set(reportedComment)
            }.addOnSuccessListener {
                Log.d(TAG, "Report count updated for post: $commentId")
                // Check if reported count is 5, and if so, permanently delete the post

            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to update report count for post: $commentId, ${e.message}", e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reporting post: $commentId, ${e.message}", e)
        }
    }

    private suspend fun increaseReportCount(userId: String) {
        firestore.runTransaction { transaction ->
            val userRef = users.document(userId)
            val snapshot = transaction.get(userRef)

            // Increment the report count
            val currentReportCount = snapshot.getLong("reportCount") ?: 0
            transaction.update(userRef, "reportCount", currentReportCount + 1)
            if (currentReportCount + 1 >= 15) {
                transaction.delete(userRef)
                // Permanently delete the user and block in Firebase Auth
                disableAccount(userId)
            }
        }.addOnSuccessListener {
            // Transaction succeeded
            Log.d(TAG, "Report count increased for user: $userId")
        }.addOnFailureListener { e ->
            // Transaction failed
            Log.e(TAG, "Failed to increase report count for user: $userId, $e")
        }
    }


    fun disableAccount(userId: String) {

            // Get the user record from Firebase Auth
            try {
                // Disable the account using Firebase Auth
                val user = FirebaseAuth.getInstance().currentUser

                user!!.delete()

                // Log success

            } catch (e: Exception) {
                // Log error
                println("Error disabling account for user: $userId, ${e.message}")
            }
    }

    // Helper function to get the report count of a user
    suspend fun getUserReportCount(userId: String): Int {
        val userSnapshot = firestore.collection("users").document(userId).get().await()
        return userSnapshot.getLong("reportCount")?.toInt() ?: 0
    }

    suspend fun reportUser(userId: String) {
        increaseReportCount(userId)
    }

    suspend fun getUser(userId: String): ActiveUser? {

        val userRef = users.document(userId).get().await()
        if (userRef.exists()) {
            val avatarName = userRef.getString("avatar") ?: ""
            val bio = userRef.getString("bio") ?: ""
            val advicePointCount = userRef.getLong("advicePoint") ?: 0L
            val followingTopic = followingTopics.document(userId).get().await()
            val followingTopicSize = followingTopic.data!!.size.toLong()
            val url = avatarList.find { it.name == avatarName }!!.url
            val postCount = userRef.getLong("postCount") ?: 0L

            return ActiveUser(
                avatar = Avatar(url, avatarName),
                advicePointCount = advicePointCount,
                followingTopicsCount = followingTopicSize,
                postCount = postCount,
                bio = bio
            )
        }else {
            return null
        }
    }

    suspend fun getUserPosts(userId: String): List<DisplayPost> {
        val userPostsCollection = users.document(userId).collection("posts")
        val postIds = mutableListOf<String>()

        try {
            // Fetch all documents from the user's posts subcollection
            val querySnapshot = userPostsCollection.get().await()

            // Extract the post IDs from the documents
            for (document in querySnapshot.documents) {
                val postId = document.getString("postId")
                log("get User Posts : postId $postId")
                if (postId != null) {
                    postIds.add(postId)
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error fetching user posts", e)
        }

        // Fetch the posts from the postsList collection using the retrieved post IDs
        val userPosts = mutableListOf<DisplayPost>()
        val postCollection = Firebase.firestore.collection("posts")

        for (postId in postIds) {
            try {
                val document = postCollection.document(postId).get().await()
                if (document.exists()) {
                    val postedUserId = document.getString("userId") ?: ""
                    val topicName = document.getString("topicName") ?: ""
                    val postContent = document.getString("postContent") ?: ""
                    val postCreatedAt = document.getLong("postCreatedAt") ?: -1L
                    val likeCount = document.getLong("likeCount") ?: 0L
                    val commentCount = document.getLong("commentCount") ?: 0L
                    val avatarName = fetchAvatarName(postedUserId)
//                    log("Avatar name from userId: $avatarName")
                    val url = avatarList.find { it.name == avatarName }!!.url

                    val likedByUser = isPostLikedByCurrentUser(document.id, userId)

                    val savedByUser = isSavedByUser(document.id, userId)

                    val post = DisplayPost(
                        postId = document.id,
                        userId = postedUserId,
                        postContent = postContent,
                        topicName = topicName,
                        likeCount = likeCount,
                        avatarName = avatarName,
                        avatarUrl = url,
                        likedByCurrentUser = likedByUser,
                        commentCount = commentCount,
                        isSavedByUser = savedByUser
                    )

                    log("posts in getUser ${post.toString()}")
                    post?.let { userPosts.add(it) }
                }
            } catch (e: Exception) {
                Log.e("AppRepository", "Error fetching post with ID $postId", e)
            }
        }

        return userPosts
    }


    suspend fun getSavedPosts(userId: String): List<DisplayPost>? {

        val userPostsCollection = users.document(userId).collection("savedPosts")
        val postIds = mutableListOf<String>()

        try {
            // Fetch all documents from the user's posts subcollection
            val querySnapshot = userPostsCollection.get().await()

            // Extract the post IDs from the documents
            for (document in querySnapshot.documents) {
                val postId = document.getString("postId")
                log("get User Posts : postId $postId")
                if (postId != null) {
                    postIds.add(postId)
                }
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Error fetching user posts", e)
        }

        // Fetch the posts from the postsList collection using the retrieved post IDs
        val userPosts = mutableListOf<DisplayPost>()
        val postCollection = Firebase.firestore.collection("posts")

        for (postId in postIds) {
            try {
                val document = postCollection.document(postId).get().await()
                if (document.exists()) {
                    val postedUserId = document.getString("userId") ?: ""
                    val topicName = document.getString("topicName") ?: ""
                    val postContent = document.getString("postContent") ?: ""
                    val postCreatedAt = document.getLong("postCreatedAt") ?: -1L
                    val likeCount = document.getLong("likeCount") ?: 0L
                    val commentCount = document.getLong("commentCount") ?: 0L
                    val avatarName = fetchAvatarName(postedUserId)
//                    log("Avatar name from userId: $avatarName")
                    val url = avatarList.find { it.name == avatarName }!!.url

                    val likedByUser = isPostLikedByCurrentUser(document.id, userId)

//                    val savedByUser = isSavedByUser(document.id, userId)

                    val post = DisplayPost(
                        postId = document.id,
                        userId = postedUserId,
                        postContent = postContent,
                        topicName = topicName,
                        likeCount = likeCount,
                        avatarName = avatarName,
                        avatarUrl = url,
                        likedByCurrentUser = likedByUser,
                        commentCount = commentCount,
                        isSavedByUser = true
                    )

                    log("posts in getUser ${post.toString()}")
                    post?.let { userPosts.add(it) }
                }
            } catch (e: Exception) {
                Log.e("AppRepository", "Error fetching post with ID $postId", e)
            }
        }
        return userPosts
    }

    suspend fun updateBio(userId: String, bio: String): String {
        return try {
            // Get reference to the user document
            val userRef = users.document(userId)

            // Update the bio field
            userRef.update("bio", bio).await()

            "Bio updated successfully"
        } catch (e: Exception) {
            Log.e(TAG, "Error updating bio for user $userId: ${e.message}", e)
            "Failed to update bio"
        }
    }

    suspend fun deleteUser(userId: String) {

        val batch = firestore.batch()

        postsList.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    batch.delete(document.reference)
                }
                // Commit the batch delete operation for posts
                batch.commit()
                    .addOnSuccessListener {
                        Log.d(TAG, "Posts deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error deleting posts: $e")
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error querying posts: $e")
            }


        // Delete comments of the user
        firestore.collection("comments").whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    batch.delete(document.reference)
                }
                // Commit the batch delete operation for comments
                batch.commit()
                    .addOnSuccessListener {
                        Log.d(TAG, "Comments deleted successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error deleting comments: $e")
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error querying comments: $e")
            }

    }


}

