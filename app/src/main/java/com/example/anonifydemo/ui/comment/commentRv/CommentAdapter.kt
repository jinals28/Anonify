package com.example.anonifydemo.ui.comment.commentRv

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.RowCommentBinding
import com.example.anonifydemo.ui.dataClasses.Comment
import com.example.anonifydemo.ui.dataClasses.DisplayComment
import com.example.anonifydemo.ui.dataClasses.DisplayLike
import de.hdodenhof.circleimageview.CircleImageView

class CommentAdapter(private val context: Context) :
    ListAdapter<DisplayComment, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    private var userLikes: MutableList<DisplayLike> = mutableListOf()

    inner class CommentViewHolder(binding : RowCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        private val userName : TextView = binding.txtusrnm
        private val userUrl : CircleImageView = binding.imgUsr
        private val content : TextView = binding.txtComment
        private val noLike : TextView = binding.Nolike

        // Initialize views here


        fun bind(comment: DisplayComment) {
            // Bind data to views here
            userName.text = comment.userName
            userUrl.setImageDrawable(ContextCompat.getDrawable(context, comment.avatarUrl))
            content.text = comment.postContent
            noLike.text = comment.likeCount.toString()

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
