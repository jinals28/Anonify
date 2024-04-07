package com.example.anonifydemo.ui.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCommentBinding
import com.example.anonifydemo.databinding.FragmentCreatePostBinding
import com.example.anonifydemo.ui.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class CommentFragment : Fragment(), Utils {

    private lateinit var noOfLike: TextView
    private lateinit var noOfComment: TextView
    private lateinit var moreOptions: ImageButton
    private lateinit var commentButton: ImageButton
    private lateinit var likeButton: ImageButton
    private lateinit var userAvatar: CircleImageView
    private lateinit var userName: TextView
    private lateinit var txtPostContent: TextView

    private lateinit var noOfComments : TextView

    private var _binding: FragmentCommentBinding?=null
    private val binding get() = _binding


    private val viewModel: CommentViewModel by viewModels()

    private val args : CommentFragmentArgs by navArgs()
    private lateinit var btnback : ImageButton

    private lateinit var txtHashtag : TextView

    private lateinit var commentEditText : EditText

    private lateinit var commentPostButton : Button

    private lateinit var commentRv : RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentCommentBinding.inflate(layoutInflater, container, false)

        val postId = args.postId

        lifecycleScope.launch {
            viewModel.getPostById(postId)
        }

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

        commentPostButton = binding!!.postCommentButton
        commentEditText = binding!!.commentEditText
        txtHashtag = binding!!.postLayout.txtHashtag
        txtPostContent = binding!!.postLayout.txtpost
        userName = binding!!.postLayout.txtusrnm
        userAvatar = binding!!.postLayout.imgUsr
        likeButton = binding!!.postLayout.likeBtn
        commentButton = binding!!.postLayout.commentBtn
        moreOptions =binding!!.postLayout.moreOptions
        noOfLike = binding!!.postLayout.Nolike
        noOfComment = binding!!.postLayout.Nocomment
        noOfComments = binding!!.noOfComments
        commentRv = binding!!.commentRv


        viewModel.post.observe(viewLifecycleOwner){ post ->

            txtHashtag.text = post.topicName

            txtPostContent.text = post.postContent

            userAvatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), post.avatarUrl))

            userName.text = post.avatarName

            noOfLike.text = post.likeCount.toString()

            noOfComment.text = post.commentCount.toString()

            noOfComments.text = post.commentCount.toString()
        }

        commentPostButton.setOnClickListener {

            val commentText = commentEditText.text.toString()

            if (commentText.isEmpty()){
                toast(requireContext(), "comments cannot be blank")
            }else{
                viewModel.postComment(commentText)
            }
        }





    private fun  goToHomeFragment() {
        val navController = findNavController()
        if (findNavController().currentDestination!!.id == R.id.commentFragment) {
            navController.popBackStack()
        }
    }




}