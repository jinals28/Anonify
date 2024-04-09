package com.example.anonifydemo.ui.choosetopics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseTopicBinding
import com.example.anonifydemo.ui.choosetopics.topicRecyclerView.TopicRecyclerViewAdapter
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.utils.Utils
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChooseTopicFragment : Fragment(), Utils {

    private var _binding : FragmentChooseTopicBinding? = null

    private val binding get() = _binding
    private lateinit var next : ImageButton
    private lateinit var topicRv : RecyclerView

    private lateinit var fabNext : FloatingActionButton

    private val userViewModel : UserViewModel by activityViewModels()

    private val followingTopicList = mutableListOf<FollowingTopic>()

    private val viewModel: ChooseTopicViewModel by viewModels()

    private var topicsList = mutableListOf<Topic>()

    private lateinit var topicAdapter : TopicRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseTopicBinding.inflate(layoutInflater, container, false)

//        lifecycleScope.launch {
//            topicsList = AppRepository.getTopics().toMutableList()
//        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topicRv = binding!!.recyclerView
        fabNext = binding!!.btnnext

//        val userId = userViewModel.getUser()!!.userId
        val userId = userViewModel.getUserId()

//        fabNext = binding!!.nextBtn

//        val topicList = resources.getStringArray(R.array.topic_names).toList()

        val topics : List<Topic> = AppRepository.topicList

        topicAdapter = TopicRecyclerViewAdapter(requireContext(), topics, followingTopicList, userId)
        topicRv.adapter = topicAdapter

//
//        topicList.forEachIndexed { index, name ->
//            val topic = Topics(id = index, name = name)
//            topics.add(topic)
//        }

        //val staggeredStaggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        topicRv.layoutManager = FlexboxLayoutManager(context).apply {
            flexDirection = FlexDirection.ROW
            flexWrap = FlexWrap.WRAP
        }

//        next.visibility=View.VISIBLE
//        topicAdapter.updateNextButtonVisibility(next)

        fabNext.setOnClickListener {
            if (topicAdapter.getSelectedTopicsCount() < 3){
                toast(requireContext(), "Select upto 3 topics to continue")
            }else{
                val followingTopicList = topicAdapter.followingTopicList
                userViewModel.saveSelectedTopics(followingTopicList)
                goToFeedFragment()
            }
//            if (topicAdapter.getSelectedTopicsCount() < 3){
//
//            }else{
////                val priorityList = topicAdapter.topicList.filter { it.priority > 0 }.sortedBy { it.priority }
//
//                userViewModel.updateUserTopic(followingTopicList)
////                Log.d("Choose com.example.anonifydemo.ui.dataClasses.Topic", priorityList.toString())
//                goToFeedFragment()
//            }
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