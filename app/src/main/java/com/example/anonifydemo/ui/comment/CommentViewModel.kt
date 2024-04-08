package com.example.anonifydemo.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.anonifydemo.ui.dataClasses.Comment
import com.example.anonifydemo.ui.dataClasses.DisplayComment
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.repository.PostManager
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {


    private val _post = MutableLiveData<DisplayPost>()

    val post : LiveData<DisplayPost> = _post

    private val _commentLiveData = MutableLiveData<List<DisplayComment>>()

    val commentsLiveData: LiveData<List<DisplayComment>> = _commentLiveData

    suspend fun getPostById(postId: String){
        viewModelScope.launch {
            _post.value = AppRepository.fetchPost(postId)
            _commentLiveData.value = AppRepository.fetchComments(postId)
        }


    }

    suspend fun postComment( userId: String, postId: String, commentText: String, onSuccess : (String) -> Unit) {

        val comment = Comment(
            userId = userId,
            postId = postId,
            commentText = commentText,
            commentedAt = System.currentTimeMillis()
        )
        AppRepository.addCommentToPost(comment, onSuccess) {
                viewModelScope.launch {
                    val post = _post.value!!.copy(commentCount = it.size.toLong())
                    _post.value = post
                    _commentLiveData.value = it
                }
            }

    }

    private val postManager = PostManager.getInstance()

//    fun getPostById(postId: Long): Post? {
//        return postManager.getPostList().find { it.postId == postId }
//    }
}