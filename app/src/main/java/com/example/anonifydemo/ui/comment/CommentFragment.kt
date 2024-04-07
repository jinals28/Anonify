package com.example.anonifydemo.ui.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCommentBinding
import com.example.anonifydemo.databinding.FragmentCreatePostBinding

class CommentFragment : Fragment() {

    private var _binding: FragmentCommentBinding?=null
    private val binding get() = _binding


    private val viewModel: CommentViewModel by viewModels()

    private val args : CommentFragmentArgs by navArgs()
    private lateinit var btnback : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentCommentBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnback = binding!!.btnback
        val postId = args.postId
        btnback.setOnClickListener {
            goToHomeFragment()
        }
//        val post = viewModel.getPostById(postId)
    }

    private fun  goToHomeFragment() {
        val navController = findNavController()
        if (findNavController().currentDestination!!.id == R.id.commentFragment) {
            navController.popBackStack()
        }
    }




}