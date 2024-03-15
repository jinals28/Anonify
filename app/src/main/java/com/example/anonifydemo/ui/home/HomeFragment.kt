package com.example.anonifydemo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.databinding.FragmentHomeBinding
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.home.postRecyclerView.PostRecyclerViewAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var postRv : RecyclerView

    private lateinit var homeViewModel: HomeViewModel

    private val userViewModel : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postRv = binding.postRv

        homeViewModel.postList.observe(viewLifecycleOwner){ posts ->
            val userTopics = userViewModel.getUser()?.topics ?: emptyList()
            val filteredPosts = posts.filter { post ->
                userTopics.any { topic ->
                    post.hashtag == topic.name
                }
            }
            val adapter = PostRecyclerViewAdapter(requireContext(), filteredPosts, userViewModel.getUser()!!)
            postRv.adapter = adapter
        }




    }

}