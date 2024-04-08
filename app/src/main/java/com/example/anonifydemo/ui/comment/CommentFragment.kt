package com.example.anonifydemo.ui.comment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Button
import android.widget.EditText

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCommentBinding
import com.example.anonifydemo.ui.comment.commentRv.CommentAdapter
import com.example.anonifydemo.ui.dataClasses.DisplayPost
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.home.postRecyclerView.PostRecyclerViewAdapter
import com.example.anonifydemo.ui.repository.AppRepository
import com.example.anonifydemo.ui.utils.Utils
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class CommentFragment : Fragment(), Utils {

    private lateinit var adapter: PostRecyclerViewAdapter
    private lateinit var noOfLike: TextView
    private lateinit var noOfComment: TextView
    private lateinit var moreOptions: ImageButton
    private lateinit var commentButton: ImageButton
    private lateinit var likeButton: ImageButton
    private lateinit var userAvatar: CircleImageView
    private lateinit var userName: TextView
    private lateinit var txtPostContent: TextView
    private lateinit var shimmerViewContainer: ShimmerFrameLayout

    private lateinit var noOfComments: TextView

    private lateinit var postRv : RecyclerView

    private lateinit var commentAdapter: CommentAdapter

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding

    private val userViewModel: UserViewModel by activityViewModels()

    private val viewModel: CommentViewModel by viewModels()

    private val args: CommentFragmentArgs by navArgs()
    private lateinit var btnback: ImageButton

    private lateinit var txtHashtag: TextView

    private lateinit var commentEditText: EditText

    private lateinit var commentPostButton: Button

    private lateinit var commentRv: RecyclerView
    private lateinit var shimmerViewContainer2: ShimmerFrameLayout
    private lateinit var displayPost: DisplayPost


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCommentBinding.inflate(layoutInflater, container, false)

        val postId = args.postId

        lifecycleScope.launch {
            viewModel.getPostById(userViewModel.getUserId(), postId)
        }

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = userViewModel.getUserId()

        adapter = PostRecyclerViewAdapter(requireContext(), userId)

        postRv.adapter = adapter
        btnback = binding!!.btnback

        val postId = args.postId

        btnback.setOnClickListener {
            goToHomeFragment()
        }
//        val post = viewModel.getPostById(postId)


    commentPostButton = binding!!.postCommentButton
    commentEditText = binding!!.commentEditText
    postRv = binding!!.postLayout
//    txtHashtag = binding!!.postLayout.txtHashtag
//    txtPostContent = binding!!.postLayout.txtpost
//    userName = binding!!.postLayout.txtusrnm
//    userAvatar = binding!!.postLayout.imgUsr
//    likeButton = binding!!.postLayout.likeBtn
//    commentButton = binding!!.postLayout.commentBtn
//    moreOptions =binding!!.postLayout.moreOptions
//    noOfLike = binding!!.postLayout.Nolike
//    noOfComment = binding!!.postLayout.Nocomment
    noOfComments = binding!!.noOfComments
    commentRv = binding!!.commentRv
        shimmerViewContainer = binding!!.shimmerViewContainer
        shimmerViewContainer2 = binding!!.shimmerViewContainer2

        // Start shimmer effect
        shimmerViewContainer.startShimmer()
        shimmerViewContainer2.startShimmer()

    commentAdapter = CommentAdapter(requireContext(), userViewModel.getUserId())

    commentRv.apply{
        layoutManager = LinearLayoutManager(requireContext())
        adapter = commentAdapter
    }



//TODO: CREATE A LIST OF LOCAL DISPLAYED POSTS FOR CACHING

    viewModel.post.observe(viewLifecycleOwner)
    { post ->
        shimmerViewContainer.stopShimmer()
        shimmerViewContainer.visibility = View.GONE
        postRv.visibility = View.VISIBLE
        displayPost = post

//        txtHashtag.text = post.topicName
//
//        txtPostContent.text = post.postContent
//
//        userAvatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), post.avatarUrl))
//
//        userName.text = post.avatarName
//
//        noOfLike.text = post.likeCount.toString()
//
//        noOfComment.text = post.commentCount.toString()
//
//        noOfComments.text = post.commentCount.toString()

        noOfComments.text = post.commentCount.toString()

        adapter.submitList(mutableListOf(post))

    }

    viewModel.commentsLiveData.observe(viewLifecycleOwner)
    {
        comments ->

        shimmerViewContainer2.stopShimmer()
        shimmerViewContainer2.visibility = View.GONE
        commentRv.visibility = View.VISIBLE
        log("Comment Fragemnet $comments.toString()")
        comments.let {
            commentAdapter.submitList(comments.toMutableList())
        }

    }
        commentPostButton.setOnClickListener{

        val commentText = commentEditText.text.toString()

        if (commentText.isEmpty()) {
            toast(requireContext(), "comments cannot be blank")
        } else {
            lifecycleScope.launch {
                viewModel.postComment(postId = displayPost.postId,
                    userId = userId,
                    commentText = commentText,
                    onSuccess = {
                        commentEditText.setText("")
                        Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                    })
            }

        }
    }
}


    private fun  goToHomeFragment() {
        val navController = findNavController()
        if (findNavController().currentDestination!!.id == R.id.commentFragment) {
            navController.popBackStack()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // Stop shimmer effect and release resources
       // shimmerViewContainer.stopShimmer()
    }

}