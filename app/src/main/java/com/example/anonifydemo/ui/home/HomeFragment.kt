package com.example.anonifydemo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
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
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), Utils {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var postRv : RecyclerView

    private lateinit var btncommunity : ImageButton
//    private lateinit var loading : ProgressBar
    private lateinit var shimmerPosts: ShimmerFrameLayout

    private val homeViewModel: HomeViewModel by viewModels()

    private val userViewModel : UserViewModel by activityViewModels()

    private lateinit var user : ActiveUser

    private lateinit var adapter : PostRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        user = userViewModel.getUser()!!

        log("home fragment, oncreate")

        lifecycleScope.launch {
            AppRepository.fetchPosts(user.uid, user.followingTopics)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btncommunity = binding.toolbarCommunityIcon
        postRv = binding.postRv
        shimmerPosts = binding.shimmerPosts
        shimmerPosts.startShimmer()
//        loading = binding.loading

        adapter = PostRecyclerViewAdapter(requireContext(), user.uid)

        postRv.adapter = adapter

        AppRepository.postList.observe(viewLifecycleOwner){ posts ->

            shimmerPosts.stopShimmer()
            shimmerPosts.visibility = View.GONE
            postRv.visibility = View.VISIBLE
            log("In Home Fragment ${posts.toString()}")
            adapter.submitList(posts.toMutableList())
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