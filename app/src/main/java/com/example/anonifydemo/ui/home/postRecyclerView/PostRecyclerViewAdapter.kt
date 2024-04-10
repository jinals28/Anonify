package com.example.anonifydemo.ui.home.postRecyclerView

import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ItemPostBinding
import com.example.anonifydemo.ui.comment.CommentFragmentDirections
import com.example.anonifydemo.ui.dataClasses.DisplayLike
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.DisplaySaved
import com.example.anonifydemo.ui.home.HomeFragmentDirections
import com.example.anonifydemo.ui.repository.AppRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PostRecyclerViewAdapter(val context : Context, val userId : String
) :  ListAdapter<DisplayPost, PostRecyclerViewAdapter.PostViewHolder>(PostDiffCallback()) {

    var userLikes: MutableList<DisplayLike> = mutableListOf()

    var savedPost : MutableList<DisplaySaved> = mutableListOf()

    init {
        updateUserLikes()
        updateSavedList()
    }

    private fun updateSavedList() {
        savedPost.clear()
        savedPost = currentList.map {

            DisplaySaved(
                postId = it.postId,
                savedAt = -1L,
                save = it.isSavedByUser
            )

        }.toMutableList()
    }

    private fun updateUserLikes() {
        userLikes.clear()
        userLikes = currentList.map {
            DisplayLike(
                postId = it.postId,
                likedAt = -1L,
                liked = it.likedByCurrentUser
            )
        }.toMutableList()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val debounceDuration = 1_000L

    inner class PostViewHolder(binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        private val txtHashtag = binding.txtHashtag
        private val txtPostContent = binding.txtpost
        private val userName = binding.txtusrnm
        private val userAvatar = binding.imgUsr
        private val likeButton = binding.likeBtn
        private val commentButton = binding.commentBtn
        private val moreOptions =binding.moreOptions
        private val noOfLike = binding.Nolike
        private val noOfComment = binding.Nocomment
        private val btnsave = binding.btnsave

        fun bind(post: DisplayPost) {

            txtHashtag.text = post.topicName

            txtPostContent.text = post.postContent

            userAvatar.setImageDrawable(ContextCompat.getDrawable(context, post.avatarUrl))

            userName.text = post.avatarName

            noOfLike.text = post.likeCount.toString()

            noOfComment.text = post.commentCount.toString()

            // Set the like button icon based on whether the user has liked the post
            setLikeButtonState(post.likedByCurrentUser)

            setSavedState(post.isSavedByUser)

            // Handle like button click
            likeButton.setOnClickListener {
                togglePost(post)
//
            }

            btnsave.setOnClickListener {
                //onsave code
                toggleSave(post)
            }

            commentButton.setOnClickListener {
                if (it.findNavController().currentDestination!!.id == R.id.navigation_home){
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToCommentFragment(postId = post.postId)
                    it.findNavController().navigate(action)
                }

            }

            userName.setOnClickListener {
                when(it.findNavController().currentDestination!!.id){
                    R.id.commentFragment -> {
                        val action =
                            CommentFragmentDirections.actionCommentFragmentToProfile2Fragment(userId = post.userId)
                        it.findNavController().navigate(action)
                    }
                    R.id.navigation_home ->{
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToProfile2Fragment(userId = post.userId)
                        it.findNavController().navigate(action)
                    }
                    else -> {}
                }
            }

            txtHashtag.setOnClickListener {
                Log.d("Anonify : RB", post.topicName)
                when(it.findNavController().currentDestination!!.id){
                    R.id.commentFragment -> {
                        val action =
                            CommentFragmentDirections.actionCommentFragmentToCommunityProfileFragment(communityName = post.topicName)
                        it.findNavController().navigate(action)
                    }
                    R.id.navigation_home ->{
                        val action =
                            HomeFragmentDirections.actionNavigationHomeToCommunityProfileFragment(communityName = post.topicName)
                        it.findNavController().navigate(action)
                    }
                    else -> {

                    }

                }

            }

            moreOptions.setOnClickListener{
                val popupMenu = PopupMenu(context, moreOptions)
                popupMenu.menuInflater.inflate(R.menu.post_popup_menu, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.block -> {
                            // Handle block post action
                            Log.d("Anonify : $TAG", "blocked")
                            if (post.userId == userId) {
                                Snackbar.make(
                                    itemView,
                                    "A user cannot report his own Post",
                                    Snackbar.ANIMATION_MODE_SLIDE
                                ).show()
                            } else {
                                Snackbar.make(itemView, "Post Reported", Snackbar.ANIMATION_MODE_SLIDE).show()
                                hidePost()
                                reportUserPost(post)
                            }
                            true
                        }
                        R.id.report -> {
                            // Handle report post action
                            Log.d("Anonify : $TAG", "reported")
                            if (post.userId == userId) {
                                Snackbar.make(
                                    it,
                                    "A user cannot report himself",
                                    Snackbar.ANIMATION_MODE_SLIDE
                                ).show()
                            } else {
                                Snackbar.make(it, "User Reported", Snackbar.ANIMATION_MODE_SLIDE).show()
                                hidePost()
                                reportUserPost(post)
                                reportUser(userId)
                            }
                            true
                        }
                        R.id.hide -> {
                            // Handle hide post action
                            Log.d("Anonify : $TAG", "hide post")
                            hidePost()
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }

        private fun reportUser(userId: String) {
            coroutineScope.launch {
                AppRepository.reportUser(userId)
            }
        }

        private fun hidePost() {
            val position = adapterPosition
            // Check if the position is valid
            if (position != RecyclerView.NO_POSITION) {
                // Apply animation
                val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)
                itemView.startAnimation(animation)
                // Remove the item from the list
                removeItem(position)
            }
        }

        private fun reportUserPost(post: DisplayPost) {

            coroutineScope.launch {
                AppRepository.reportPost(this, userId,post.userId,  post.postId)
            }


        }

        fun removeItem(position: Int) {
            if (position != RecyclerView.NO_POSITION) {
                // Remove the item from the list
                // Notify the adapter about the item removal
                val newList = currentList.toMutableList()
                newList.removeAt(position)
                // Submit the new list to the ListAdapter
                submitList(newList)
            }
        }


        private fun toggleSave(post: DisplayPost) {

            val isSaved = savedPost.find { it.postId == post.postId }?.save ?: false
            if (isSaved!!){
                unSavePost(post)
//                userLikes.removeAll { it.postId == post.postId }
            } else {
                savePost(post)

            }
            debounceSaveUpdateToDatabase(savedPost)
        }

        private fun setSavedState(savedByUser: Boolean) {
            if (savedByUser) {
                btnsave.setImageResource(R.drawable.baseline_bookmark_24)
            } else {
                btnsave.setImageResource(R.drawable.baseline_bookmark_border_24)
            }
        }
        private fun togglePost(post: DisplayPost) {
            Log.d(TAG, userLikes.toString())
            val isLiked = userLikes.find { it.postId == post.postId }?.liked
            Log.d(TAG, isLiked.toString())
            if (isLiked!!){
                unlikePost(post)
//                userLikes.removeAll { it.postId == post.postId }
            } else {
                likePost(post)

            }
            debounceUpdateToDatabase(userLikes)
        }

        private fun setLikeButtonState(isLiked: Boolean) {
            if (isLiked) {
                likeButton.setImageResource(R.drawable.baseline_thumb_up_alt_24)
            } else {
                likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
            }
        }
        private fun setLikeCountText(likeCount: Long) {
            noOfLike.text = likeCount.toString()
        }
        private fun likePost(post: DisplayPost) {
            post.likeCount++
            post.likedByCurrentUser = true
            setLikeButtonState(true)
            setLikeCountText(post.likeCount)
            userLikes.find { it.postId == post.postId }?.apply {
                liked = true
                likedAt = System.currentTimeMillis()
            }
        }
        private fun unlikePost(post: DisplayPost) {
            setLikeButtonState(false)
            post.likeCount--
            post.likedByCurrentUser = false
            userLikes.find { it.postId == post.postId }?.apply {
                liked = false

            }
            Log.d(TAG, userLikes.toString())
            // Update the UI
            setLikeCountText(post.likeCount)

        }

        private fun savePost(post: DisplayPost) {
            post.isSavedByUser = true
            setSavedState(true)
            savedPost.find { it.postId == post.postId }?.apply {
                save = true
                savedAt = System.currentTimeMillis()
            }
        }

        private fun unSavePost(post: DisplayPost) {
            post.isSavedByUser = false
            setSavedState(false)
            savedPost.find { it.postId == post.postId }?.apply {
                save = false
                savedAt = System.currentTimeMillis()
            }
        }

        private fun debounceUpdateToDatabase(userLikes: MutableList<DisplayLike>) {
            coroutineScope.coroutineContext.cancelChildren()
            coroutineScope.launch {
                delay(debounceDuration)
                updateLikesToDatabase(userLikes)
            }
        }
        private fun debounceSaveUpdateToDatabase(userLikes: MutableList<DisplaySaved>) {

            coroutineScope.coroutineContext.cancelChildren()

            coroutineScope.launch {
                delay(debounceDuration)
                updateSavesToDatabase(userLikes)
            }
        }
        private fun updateSavesToDatabase(userLikes: MutableList<DisplaySaved>) {
            AppRepository.updateSavedPosts(userId, userLikes)
        }
        private suspend fun updateLikesToDatabase(userLikes: MutableList<DisplayLike>) {
            Log.d(TAG, "in updateLikes ${userLikes.toString()}")
            AppRepository.updatesLikes(userId, userLikes)
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<DisplayPost>() {
        override fun areItemsTheSame(oldItem: DisplayPost, newItem: DisplayPost): Boolean {
            return oldItem.postId == newItem.postId
        }

        override fun areContentsTheSame(oldItem: DisplayPost, newItem: DisplayPost): Boolean {
            return  oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = getItem(position)

        holder.bind(post)


    }

    override fun submitList(list: MutableList<DisplayPost>?) {
        super.submitList(list)

        list?.let {
            updateUserLikes()
            updateSavedList()
        }
    }

    companion object {
        const val TAG = "Anonify: PostRecyclerViewAdapter"
    }
}