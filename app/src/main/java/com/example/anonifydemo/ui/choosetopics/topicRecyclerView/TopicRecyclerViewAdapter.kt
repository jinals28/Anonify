package com.example.anonifydemo.ui.choosetopics.topicRecyclerView

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ItemAddTopicBinding
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.dataClasses.User
import com.example.anonifydemo.ui.dataClasses.UserViewModel


class TopicRecyclerViewAdapter(
    val context: Context,
    val topicList: List<Topic>,
    val followingTopicList: MutableList<FollowingTopic>,
    val userId : Long
) : RecyclerView.Adapter<TopicRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding : ItemAddTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        private val topicName = binding.addtopic
        private val card = binding.card

        init {
//            itemView.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION){
//                    val selectedTopic = topicList[position]
//
//
//                    //!selectedTopic.isSelected refers to checking a new topic which is not selected, so in case when three topics are already selected and a new topic is selected, then it will prevent it from selection
////                    if (!selectedTopic.isSelected && getSelectedTopicsCount() >= 3){
////                        return@setOnClickListener
////                    }
////                    selectedTopic.isSelected = !selectedTopic.isSelected
////                    if (selectedTopic.isSelected){
////                        selectedTopic.priority = getSelectedTopicsCount()
////
////                    }else {
////                        selectedTopic.priority = 0
////                        reorderTopics()
//                    }
//
//                }
            card.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val selectedTopic = topicList[position]

                    //!isTopicSelected(selectedTopic) == true when a topic is not selected
                    //!isTopicSelected(selectedTopic) == false when a topic is  selected
//                    getSelectedTopicsCount() >= 3 is true when followingList ahs 3 items
//                    getSelectedTopicsCount() >= 3 is false when followingList does not have 3 items
                    if (!isTopicSelected(selectedTopic) && getSelectedTopicsCount() >= 3){
                        return@setOnClickListener
                    }
                    toggleTopicSelection(selectedTopic)
                }
            }

        }


        fun onBind(topic : Topic){
            val isSelected = isTopicSelected(topic)
            topicName.text = topic.name
            card.isSelected = isSelected
            card.setOnClickListener {
                toggleTopicSelection(topic)
//                onTopicSelected(topic)
            }
//            itemView.isSelected = topic.isSelected
            card.setCardBackgroundColor(if (isSelected) ContextCompat.getColor(context, R.color.btn) else ContextCompat.getColor(context, R.color.unselected_topic))
        }

//        private fun onTopicSelected(topic: Topic) {
//            card.setCardBackgroundColor(if (isSelected) ContextCompat.getColor(context, R.color.btn) else ContextCompat.getColor(context, R.color.unselected_topic))
//        }

        private fun toggleTopicSelection(topic: Topic) {
            val existingIndex = followingTopicList.indexOfFirst { it.topic == topic.name }
            if (existingIndex != -1) {
                followingTopicList.removeAt(existingIndex)
                Log.d("Anonify: Priority", followingTopicList.toString())
            } else {
                if (followingTopicList.size >= 3) {
//                    val firstSelected = followingTopicList.firstOrNull()
//                    followingTopicList.removeAt(0)
//                    firstSelected?.let {
//                        followingTopicList.add(it.copy(topicId = topic.topicId))
//                    }
//                    reorderFollowingTopics()
                } else {
                    followingTopicList.add(FollowingTopic(
                        topic = topic.name,
                        followedAt = System.currentTimeMillis()
                    ))
                    Log.d("Anonify: Priority", followingTopicList.toString())
                }
            }
            notifyDataSetChanged()
        }

        private fun isTopicSelected(topic: Topic): Boolean {

            return followingTopicList.any { it.topic == topic.name }

        }
    }

//    private fun reorderTopics() {
//        val selectedTopics = topicList.filter { it.isSelected }.sortedBy { it.priority }
//        Log.d("Trv", "Reorder topics: $selectedTopics")
//        selectedTopics.forEachIndexed { index, topic ->
//            topic.priority = index + 1
//        }
//        Log.d("Trv", "Reorder topics after incrementing: $selectedTopics")
//        val priorityList = topicList.filter { it.priority > 0 }
//        Log.d("Trv", priorityList.toString())
//        notifyDataSetChanged()
//    }

        fun getSelectedTopicsCount(): Int {
        return followingTopicList.size

    }
//        fun updateNextButtonVisibility(next: ImageButton) {
//            val selectedTopicsCount = getSelectedTopicsCount()
//            if (selectedTopicsCount >= 3) {
//                next.visibility = View.VISIBLE
//            } else {
//                next.visibility = View.GONE
//            }
//        }
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
//        val layoutParams = holder.itemView.layoutParams as LinearLayoutManager.LayoutParams
//        layoutParams.width = LinearLayoutManager.LayoutParams.WRAP_CONTENT
//        holder.itemView.layoutParams = layoutParams
    }
}