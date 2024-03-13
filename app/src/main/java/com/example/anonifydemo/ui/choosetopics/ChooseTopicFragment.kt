package com.example.anonifydemo.ui.choosetopics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseTopicBinding
import com.example.anonifydemo.ui.choosetopics.topicRecyclerView.TopicRecyclerViewAdapter
import com.example.anonifydemo.ui.dataClasses.Topics
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChooseTopicFragment : Fragment() {

    private var _binding : FragmentChooseTopicBinding? = null

    private val binding get() = _binding
    private lateinit var next : ImageButton
    private lateinit var topicRv : RecyclerView

    private lateinit var fabNext : FloatingActionButton

    //   private lateinit var viewModel: ChooseTopicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTopicBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topicRv = binding!!.recyclerView
        next = binding!!.btnnext

        fabNext = binding!!.nextBtn

        val topicList = resources.getStringArray(R.array.topic_names).toList()

        val topics : MutableList<Topics> = mutableListOf()

        topicList.forEachIndexed { index, name ->
            val topic = Topics(id = index, name = name)
            topics.add(topic)
        }
        val topicAdapter = TopicRecyclerViewAdapter(requireContext(), topics)

        val staggeredStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        topicRv.apply {
            layoutManager = staggeredStaggeredGridLayoutManager
            adapter = topicAdapter
        }
//        next.visibility=View.VISIBLE
        topicAdapter.updateNextButtonVisibility(next)

        fabNext.setOnClickListener {

            val priorityList = topicAdapter.topicList.filter { it.priority > 0 }.sortedBy { it.priority }

            Log.d("Choose Topic", priorityList.toString())
            goToFeedFragment()
        }

    }

    private fun goToFeedFragment() {
        if (findNavController().currentDestination!!.id == R.id.chooseTopic){
            val action = ChooseTopicFragmentDirections.actionChooseTopicToFeedFragment()
            findNavController().navigate(action)
        }
    }

}