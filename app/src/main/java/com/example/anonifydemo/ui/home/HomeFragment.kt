package com.example.anonifydemo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentHomeBinding
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.home.postRecyclerView.PostRecyclerViewAdapter
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.utils.Utils
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), Utils {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var postRv : RecyclerView

    private lateinit var btncommunity : ImageButton

    private val homeViewModel: HomeViewModel by viewModels()

    private val userViewModel : UserViewModel by activityViewModels()

    private lateinit var user : ActiveUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        user = userViewModel.getUser()!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btncommunity = binding.toolbarCommunityIcon
        postRv = binding.postRv

        lifecycleScope.launch {
            AppRepository.fetchPosts(user.followingTopics)
        }

        AppRepository.postList.observe(viewLifecycleOwner){ posts ->
//            val userTopics = userViewModel.getUser()?.topics ?: emptyList()
//            val filteredPosts = posts.filter { post ->
//                userTopics.any { topic ->
//                    post.hashtag == topic.name
//                }
//            }
//            val adapter = PostRecyclerViewAdapter(requireContext(), posts, userViewModel.getUser()!!)
            log("In Home Fragment ${posts.toString()}")
            val adapter = PostRecyclerViewAdapter(requireContext(), posts)
            postRv.adapter = adapter
        }

        btncommunity.setOnClickListener {
            goToCommunityFragment()
        }

    }
    private fun goToCommunityFragment() {
        if (findNavController().currentDestination!!.id == R.id.navigation_home) {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchCommunityFragment()
            findNavController().navigate(action)
        }
    }

}