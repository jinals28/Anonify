package com.example.anonifydemo.ui.choosetopics.topicRecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.databinding.ItemAddTopicBinding

class TopicRecyclerViewAdapter(val context : Context, val topicList : List<String>) : RecyclerView.Adapter<TopicRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemAddTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        private val topicName = binding.addtopic

        fun onBind(name : String){
            topicName.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAddTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return topicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val topicName = topicList[position]
        holder.onBind(topicName)
    }
}