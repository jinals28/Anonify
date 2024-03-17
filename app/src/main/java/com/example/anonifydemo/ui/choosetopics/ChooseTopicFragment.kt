package com.example.anonifydemo.ui.choosetopics

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseTopicBinding
import com.example.anonifydemo.ui.choosetopics.topicRecyclerView.TopicRecyclerViewAdapter
import com.example.anonifydemo.ui.dataClasses.Topics
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.utils.Utils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChooseTopicFragment : Fragment(), Utils {

    private var _binding : FragmentChooseTopicBinding? = null

    private val binding get() = _binding
    private lateinit var next : ImageButton
    private lateinit var topicRv : RecyclerView

    private lateinit var fabNext : FloatingActionButton

    private val userViewModel : UserViewModel by activityViewModels()

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
        fabNext = binding!!.btnnext

        userViewModel.user.observe(viewLifecycleOwner){

        }

//        fabNext = binding!!.nextBtn

        val topicList = resources.getStringArray(R.array.topic_names).toList()

        val topics : MutableList<Topics> = mutableListOf()

        topicList.forEachIndexed { index, name ->
            val topic = Topics(id = index, name = name)
            topics.add(topic)
        }
        val topicAdapter = TopicRecyclerViewAdapter(requireContext(), topics)

        //val staggeredStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        topicRv.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }
        topicRv.apply {
           // layoutManager = staggeredStaggeredGridLayoutManager
            adapter = topicAdapter
        }
//        next.visibility=View.VISIBLE
//        topicAdapter.updateNextButtonVisibility(next)

        fabNext.setOnClickListener {
            if (topicAdapter.getSelectedTopicsCount() < 3){
                toast(requireContext(), "Select upto 3 topics to continue")
            }else{
                val priorityList = topicAdapter.topicList.filter { it.priority > 0 }.sortedBy { it.priority }

                userViewModel.updateUserTopic(priorityList)
                Log.d("Choose com.example.anonifydemo.ui.dataClasses.Topic", priorityList.toString())
                goToFeedFragment()
            }
        }

//        fabNext.setOnClickListener {
//
//
//        }
//
//        next.setOnClickListener {
//
//
//            goToFeedFragment()
//        }

    }

    private fun goToFeedFragment() {
        if (findNavController().currentDestination!!.id == R.id.chooseTopic){
            val action = ChooseTopicFragmentDirections.actionChooseTopicToNavigationHome()
            findNavController().navigate(action)
        }
    }

}