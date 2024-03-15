package com.example.anonifydemo.ui.home.postRecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.databinding.ItemPostBinding
import com.example.anonifydemo.ui.dataClasses.Post
import com.example.anonifydemo.ui.dataClasses.User
import com.example.anonifydemo.ui.dataClasses.UserViewModel

class PostRecyclerViewAdapter(val context : Context, val postList : List<Post>, val user: User) :  RecyclerView.Adapter<PostRecyclerViewAdapter.PostViewHolder>(){
    inner class PostViewHolder(binding : ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

        private val txtHashtag = binding.txtHashtag
        private val txtPostContent = binding.txtpost
        private val userName = binding.txtusrnm
        private val userAvatar = binding.imgUsr

        fun bind(post: Post) {

            txtHashtag.text = post.hashtag
            txtPostContent.text = post.text
            if (user.uid == post.uid){

                userAvatar.setImageDrawable(ContextCompat.getDrawable(context, user.avatarUrl.id))
                userName.text = user.avatarUrl.name
            }

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
}