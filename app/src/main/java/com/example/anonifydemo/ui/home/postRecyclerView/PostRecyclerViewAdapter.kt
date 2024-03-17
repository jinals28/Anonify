package com.example.anonifydemo.ui.home.postRecyclerView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ItemPostBinding
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.User
import com.example.anonifydemo.ui.home.HomeFragmentDirections
import com.example.anonifydemo.ui.repository.PostManager
import kotlin.math.min

class PostRecyclerViewAdapter(val context : Context, val postList : List<Post>, val user: User) :  RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>(){
    inner class PostViewHolder(binding : ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        private val txtHashtag = binding.txtHashtag
        private val txtPostContent = binding.txtpost
        private val userName = binding.txtusrnm
        private val userAvatar = binding.imgUsr
        private val likeButton = binding.likeBtn
        private val commentButton = binding.commentBtn

        init {

        }


        fun bind(post: Post) {

            txtHashtag.text = post.hashtag
            txtPostContent.text = post.text

            if (user.uid == post.uid){

//                userAvatar.setImageDrawable(ContextCompat.getDrawable(context, user.avatarUrl.id))
//                userName.text = user.avatarUrl.name
            }

            // Set the like button icon based on whether the user has liked the post
            setLikeButtonState(post.likedBy.contains(user.uid))

            // Set the like count text
            setLikeCountText(post.likedBy, post.likeCount)

            // Handle like button click
            likeButton.setOnClickListener {
                if (post.likedBy.contains(user.uid)) {
                    // Unlike the post
                    unlikePost(post)
                } else {
                    // com.example.anonifydemo.ui.dataClasses.Like the post
                    likePost(post)
                }
            }

            commentButton.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToCommentFragment(postId = post.postId)
                    it.findNavController().navigate(action)
            }

        }

        private fun setLikeButtonState(isLiked: Boolean) {
            if (isLiked) {
                likeButton.setImageResource(R.drawable.baseline_thumb_up_alt_24)
            } else {
                likeButton.setImageResource(R.drawable.baseline_thumb_up_off_alt_24)
            }
        }

        private fun setLikeCountText(likedBy: MutableList<String>, likeCount: Int) {
            if (likeCount > 2) {
                // Show the total like count if more than 2 likes
//                likeButton.text = "$likeCount likes"
                Log.d("Anonify : $TAG", "$likeCount likes")
            } else {
                // Show the names of users who liked the post if less than 3 likes
                // Replace "likedByUser1, likedByUser2, likedByUser3" with the actual user names
                val likedByUsers = likedBy.subList(0, min(likedBy.size, 3)).joinToString(", ")
//                likeButton.text =
                Log.d("Anonify : $TAG", "$likedByUsers and ${likeCount - likedBy.size} others")
            }
        }

        private fun likePost(post: Post) {
            post.likeCount++
            post.likedBy.add(user.uid)
            // Update the UI
            setLikeButtonState(true)
            setLikeCountText(post.likedBy, post.likeCount)
            // Update the PostManager
            PostManager.updatePost(post)
        }

        private fun unlikePost(post: Post) {
            post.likeCount--
            post.likedBy.remove(user.uid)
            // Update the UI
            setLikeButtonState(false)
            setLikeCountText(post.likedBy, post.likeCount)
            // Update the PostManager
            PostManager.updatePost(post)
        }

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {

        val post = postList[position]
        holder.bind(post)

    }

    companion object {
        const val TAG = "PostRecyclerViewAdapter"
    }
}