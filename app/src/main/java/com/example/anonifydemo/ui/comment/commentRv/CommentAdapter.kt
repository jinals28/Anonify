package com.example.anonifydemo.ui.comment.commentRv

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.RowCommentBinding
import com.example.anonifydemo.ui.dataClasses.Comment
import com.example.anonifydemo.ui.dataClasses.DisplayComment
import com.example.anonifydemo.ui.dataClasses.DisplayCommentLike
import com.example.anonifydemo.ui.dataClasses.DisplayLike
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.home.postRecyclerView.PostRecyclerViewAdapter
import com.example.anonifydemo.ui.repository.AppRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CommentAdapter(private val context: Context, private val userId : String) :
    ListAdapter<DisplayComment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    private var userLikes: MutableList<DisplayCommentLike> = mutableListOf()

    init {
        updateLikesList()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val debounceDuration = 10_000L

    override fun submitList(list: MutableList<DisplayComment>?) {
        super.submitList(list)

        list?.let {
            updateLikesList()
        }
    }

    private fun updateLikesList() {
        userLikes.clear()
        currentList.forEach { comment ->
            userLikes.add(DisplayCommentLike(comment.commentId, -1L, comment.likedByUser))
        }
    }

    inner class CommentViewHolder(binding : RowCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val userName : TextView = binding.txtusrnm
        private val userUrl : CircleImageView = binding.imgUsr
        private val content : TextView = binding.txtComment
        private val noLike : TextView = binding.Nolike
        private val btnLike : ImageButton = binding.btnlike
        // Initialize views here


        fun bind(comment: DisplayComment) {
            // Bind data to views here
            userName.text = comment.userName
            userUrl.setImageDrawable(ContextCompat.getDrawable(context, comment.avatarUrl))
            content.text = comment.postContent
            noLike.text = comment.likeCount.toString()

            setLikeButtonState(comment.likedByUser)

            btnLike.setOnClickListener {

                toggleComment(comment)
            }


        }

        private fun setLikeButtonState(likedByUser: Boolean) {
            if (likedByUser) {
                btnLike.setImageResource(R.drawable.baseline_thumb_up_alt_24)
            } else {
                btnLike.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
            }
        }

        private fun toggleComment(comment: DisplayComment) {
//            Log.d(P.TAG, userLikes.toString())
            val isLiked = userLikes.find { it.commentId == comment.commentId }?.liked
//            Log.d(PostRecyclerViewAdapter.TAG, isLiked.toString())
            if (isLiked!!){
                unlikePost(comment)
//                userLikes.removeAll { it.postId == post.postId }
            } else {
                likePost(comment)

            }
            debounceUpdateToDatabase(userLikes)

        }

        private fun debounceUpdateToDatabase(userLikes: MutableList<DisplayCommentLike>) {
            coroutineScope.coroutineContext.cancelChildren()

            coroutineScope.launch {
                delay(debounceDuration)
                updateLikesToDatabase(userLikes)
            }
        }

        private suspend fun updateLikesToDatabase(userLikes: MutableList<DisplayCommentLike>) {

            AppRepository.updateCommentLikes(userId, userLikes)
        }

        private fun setLikeCountText(likeCount: Long) {
            noLike.text = likeCount.toString()
        }

        private fun likePost(comment: DisplayComment) {
            comment.likeCount++
            comment.likedByUser = true
            setLikeButtonState(true)
            setLikeCountText(comment.likeCount)
            userLikes.find { it.commentId == comment.commentId }?.apply {
                liked= true
                likedAt = System.currentTimeMillis()
            }
        }

        private fun unlikePost(comment: DisplayComment) {
            setLikeButtonState(false)
            comment.likeCount--
            comment.likedByUser = false
            userLikes.find { it.commentId == comment.commentId }?.apply {
                liked = false
                likedAt = -1
            }
            Log.d(PostRecyclerViewAdapter.TAG, userLikes.toString())
            // Update the UI
            setLikeCountText(comment.likeCount)
        }

    }

    class CommentDiffCallback : DiffUtil.ItemCallback<DisplayComment>() {
        override fun areItemsTheSame(oldItem: DisplayComment, newItem: DisplayComment): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: DisplayComment, newItem: DisplayComment): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = RowCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        Log.d("Anonify: CommentAdapter", comment.toString())
        holder.bind(comment)
    }

}
