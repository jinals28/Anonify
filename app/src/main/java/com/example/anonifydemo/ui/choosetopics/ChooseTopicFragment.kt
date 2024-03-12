package com.example.anonifydemo.ui.choosetopics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseTopicBinding
import com.example.anonifydemo.ui.choosetopics.topicRecyclerView.TopicRecyclerViewAdapter

class ChooseTopicFragment : Fragment() {

    private var _binding : FragmentChooseTopicBinding? = null

    private val binding get() = _binding

    private lateinit var topicRv : RecyclerView

    //   private lateinit var viewModel: ChooseTopicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseTopicBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topicRv = binding!!.recyclerView

        val topicList = resources.getStringArray(R.array.topic_names).toList()

        val topicAdapter = TopicRecyclerViewAdapter(requireContext(), topicList)

        topicRv.adapter = topicAdapter

    }

}