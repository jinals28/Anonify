package com.example.anonifydemo.ui.choosetopics.topicRecyclerView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ItemAddTopicBinding
import com.example.anonifydemo.ui.choosetopics.ChooseTopicFragment
import com.example.anonifydemo.ui.dataClasses.Topics

    class TopicRecyclerViewAdapter(val context: Context, val topicList: MutableList<Topics>) : RecyclerView.Adapter<TopicRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemAddTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        private val topicName = binding.addtopic
        private val card = binding.card

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val selectedTopic = topicList[position]

                    //!selectedTopic.isSelected refers to checking a new topic which is not selected, so in case when three topics are already selected and a new topic is selected, then it will prevent it from selection
                    if (!selectedTopic.isSelected && getSelectedTopicsCount() >= 3){
                        return@setOnClickListener
                    }
                    selectedTopic.isSelected = !selectedTopic.isSelected
                    if (selectedTopic.isSelected){
                        selectedTopic.priority = getSelectedTopicsCount()
                        Log.d("Priority", selectedTopic.priority.toString())
                    }else {
                        selectedTopic.priority = 0
                        reorderTopics()
                    }
                    notifyItemChanged(position)
                }
            }
        }

        fun onBind(topic : Topics){
            topicName.text = topic.name
            itemView.isSelected = topic.isSelected
            card.setBackgroundColor(if (topic.isSelected) ContextCompat.getColor(context, R.color.btn) else ContextCompat.getColor(context, R.color.unselected_topic))
        }
    }

    private fun reorderTopics() {
        val selectedTopics = topicList.filter { it.isSelected }.sortedBy { it.priority }
        Log.d("Trv", "Reorder topics: $selectedTopics")
        selectedTopics.forEachIndexed { index, topic ->
            topic.priority = index + 1
        }
        Log.d("Trv", "Reorder topics after incrementing: $selectedTopics")
        val priorityList = topicList.filter { it.priority > 0 }
        Log.d("Trv", priorityList.toString())
        notifyDataSetChanged()
    }

    private fun getSelectedTopicsCount(): Int {
        return topicList.count { it.isSelected }

    }
        fun updateNextButtonVisibility(next: ImageButton) {
            val selectedTopicsCount = getSelectedTopicsCount()
            if (selectedTopicsCount >= 3) {
                next.visibility = View.VISIBLE
            } else {
                next.visibility = View.GONE
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