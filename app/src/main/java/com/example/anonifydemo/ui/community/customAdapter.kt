package com.example.anonifydemo.ui.community

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.anonifydemo.R

class customAdapter(context: Context, objects: Array<String>) : ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent).apply {
            // Customize the view if needed
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.choose_hashtag, parent, false)
        val textView = view.findViewById<TextView>(R.id.addhashtag)
        textView.text = getItem(position)
        return view
    }

}