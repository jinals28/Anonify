package com.example.anonifydemo.ui.community.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentSearchCommunityBinding
import com.example.anonifydemo.ui.community.search.adapter.TopicListAdapter
import com.example.anonifydemo.ui.dataClasses.Topic
import com.example.anonifydemo.ui.repository.AppRepository

class searchCommunityFragment : Fragment() {

    private var _binding: FragmentSearchCommunityBinding? = null
    private val binding get() = _binding

    //    private lateinit var viewModel: SearchCommunityViewModel
    private lateinit var btnback : ImageButton
    private lateinit var btnhash : ImageButton
    private lateinit var searchHash : SearchView

    private lateinit var rv : RecyclerView

    private lateinit var topicListAdapter: TopicListAdapter

    private lateinit var topicList: MutableList<Topic>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchCommunityBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnback = binding!!.btnback
        btnhash = binding!!.btnCreateHash
        searchHash=binding!!.searchHash

        initViews()
        setupRecyclerView()
        setupSearchView()
        btnback.setOnClickListener {
            goToHomeFragment()
        }
        btnhash.setOnClickListener {
            goToCreateHashFragment()
        }


    }

    private fun setupSearchView() {
        binding!!.searchHash.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                topicListAdapter.filter.filter(newText)
                return false
            }
        })
    }

    private fun setupRecyclerView() {
        topicListAdapter = TopicListAdapter(topicList) { topic ->
            // Handle item click event, navigate to community fragment with topic name as argument
            val action = searchCommunityFragmentDirections.actionSearchCommunityFragmentToCommunityProfileFragment(topic.name)
            findNavController().navigate(action)
        }
        binding!!.recyclerView.apply {
            adapter = topicListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }
    }

    private fun initViews() {
        topicList = AppRepository.topicList
    }

    private fun goToCreateHashFragment() {
        if (findNavController().currentDestination!!.id == R.id.searchCommunityFragment) {
            val action = searchCommunityFragmentDirections.actionSearchCommunityFragmentToCreateCommunityFragment()
            findNavController().navigate(action)
        }
    }

    private fun  goToHomeFragment() {
        val navController = findNavController()
        if (findNavController().currentDestination!!.id == R.id.searchCommunityFragment) {
//            val action = searchCommunityFragmentDirections.actionSearchCommunityFragmentToNavigationHome()
//            findNavController().navigate(action)
            navController.popBackStack()
        }
    }
}