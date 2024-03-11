package com.example.anonifydemo.ui.createPost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.google.android.material.chip.Chip

class SuggestionsAdapter(private val suggestions: List<SuggestionItem>, private val onItemClick: (SuggestionItem) -> Unit) : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_pickahashtag, parent, false)
        return ViewHolder(view as Chip)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(suggestions[position], onItemClick)
    }

    override fun getItemCount(): Int {
        return suggestions.size
    }

    class ViewHolder(val chip: Chip) : RecyclerView.ViewHolder(chip) {
        fun bind(item: SuggestionItem, onItemClick: (SuggestionItem) -> Unit) {
            chip.text = item.text
            chip.setOnClickListener { onItemClick(item) }
        }
    }
}