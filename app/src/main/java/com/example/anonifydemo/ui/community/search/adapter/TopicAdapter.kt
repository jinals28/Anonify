package com.example.anonifydemo.ui.community.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.databinding.ChooseHashtagBinding
import com.example.anonifydemo.ui.dataClasses.Topic
import java.util.Locale

class TopicListAdapter(
    private val originalList: List<Topic>,
    private val onItemClick: (Topic) -> Unit
) : RecyclerView.Adapter<TopicListAdapter.TopicViewHolder>(), Filterable {

    private var filteredList: List<Topic> = originalList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val itemView = ChooseHashtagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = filteredList[position]
        holder.bind(topic)
        holder.itemView.setOnClickListener {
            onItemClick(topic)
        }
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = mutableListOf<Topic>()
                val query = constraint?.toString()?.lowercase(Locale.getDefault()) ?: ""
                if (query.isEmpty()) {
                    filteredResults.addAll(originalList)
                } else {
                    for (topic in originalList) {
                        if (topic.name.lowercase(Locale.getDefault()).contains(query)) {
                            filteredResults.add(topic)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as? List<Topic> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

    class TopicViewHolder(itemView: ChooseHashtagBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val topicTextView: TextView = itemView.addhashtag

        fun bind(topic: Topic) {
            topicTextView.text = topic.name
        }
    }
}
