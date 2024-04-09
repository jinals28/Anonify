package com.example.anonifydemo.ui.displayProfile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCommentBinding
import com.example.anonifydemo.databinding.FragmentProfile2Binding
import com.example.anonifydemo.databinding.FragmentProfileBinding
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.home.postRecyclerView.PostRecyclerViewAdapter
import com.example.anonifydemo.ui.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class Profile2Fragment : Fragment(), Utils {

    private val args : Profile2FragmentArgs by navArgs()

    private var _binding: FragmentProfile2Binding? = null
    private val binding get() = _binding


    private val userViewModel : UserViewModel by activityViewModels()

    private val viewModel: Profile2ViewModel by viewModels()

    private lateinit var imgUser : CircleImageView
    private lateinit var userName : TextView
    private lateinit var followingTopicCount : TextView
    private lateinit var postCount : TextView
    private lateinit var advicePointCount : TextView
    private lateinit var postBtn : Button
    private lateinit var rv : RecyclerView

    private lateinit var adapter : PostRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfile2Binding.inflate(layoutInflater, container, false)

        val userId = args.userId

        lifecycleScope.launch {
            viewModel.getUser(userId)
        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgUser = binding!!.imgUsr
        userName = binding!!.txtusrnm
        followingTopicCount = binding!!.txtfollowing
        advicePointCount = binding!!.txtviews
        postCount = binding!!.txtpo
        rv = binding!!.rv

        adapter = PostRecyclerViewAdapter(requireContext(), userViewModel.getUserId())

        rv.adapter = adapter

        viewModel.currentUser.observe(viewLifecycleOwner){

            imgUser.setImageDrawable(ContextCompat.getDrawable(requireContext(), it.avatar.url))

            userName.text = it.avatar.name

            followingTopicCount.text = it.followingTopicsCount.toString()

            advicePointCount.text = it.advicePointCount.toString()

            postCount.text = it.postCount.toString()

        }
        
        viewModel.postList.observe(viewLifecycleOwner){
            log("profile fragment $it")
            adapter.submitList(it.toMutableList())
        }


    }



}