package com.example.anonifydemo.ui.createPost

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.databinding.FragmentCreatePostBinding
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.FollowingTopic
import com.google.android.material.chip.Chip
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class CreatePostFragment : Fragment(), Utils {

    private var _binding: FragmentCreatePostBinding?=null
    private val binding get() = _binding

    private val userViewModel : UserViewModel by activityViewModels()

    private val postViewModel : CreatePostViewModel by viewModels()

    private lateinit var userAvatar : CircleImageView

    private lateinit var suggestionList : List<FollowingTopic>

    private lateinit var suggestionRv : RecyclerView

   private lateinit var hashtagChip: Chip

    private lateinit var textInput: EditText

    private lateinit var postContent : EditText

    private lateinit var postBtn : Button

    private var avatar : Int = -1

    private var userId : String = ""

    private lateinit var activeUser: ActiveUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentCreatePostBinding.inflate(layoutInflater, container, false)

        userId = userViewModel.getUserId()

        lifecycleScope.launch {

            postViewModel.getFollowinTopics(userId)

        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activeUser = userViewModel.getUser()!!

        suggestionRv = binding!!.suggestionsRecyclerView

        hashtagChip = binding!!.hashtagChip

        textInput = binding!!.textInput

        postContent = binding!!.postContent

        userAvatar = binding!!.imgUsr

        postBtn = binding!!.toolbarButtonPost

        avatar = activeUser.avatar.url

        userId = activeUser.uid

        userAvatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), avatar))

//        suggestionList = AppRepository.topicList

//        uid = userViewModel.getUser()!!.uid

        postViewModel.setTopicList(activeUser.followingTopics)


       postViewModel.topicList.observe(viewLifecycleOwner){ suggestionsList ->
           log("suggestionList ${suggestionsList.toString()}")
            suggestionList = suggestionsList
            val suggestionsAdapter = SuggestionsAdapter(suggestionsList) { suggestionItem ->

                binding!!.textInput.setText("${suggestionItem.topic}")

                hashtagChip.text = suggestionItem.topic

                hideSuggestions()
            }
            suggestionRv.adapter = suggestionsAdapter
        }


       // Set the cursor after the '#'
//        setupSuggestionsRecyclerView()

        hashtagChip.setOnClickListener {
            hashtagChip.visibility = View.GONE
            textInput.visibility = View.VISIBLE
            textInput.requestFocus()
            textInput.setText("#")
            textInput.setSelection(1)
            showKeyboard()
        }

        textInput.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                hashtagChip.visibility = View.VISIBLE
                textInput.visibility = View.GONE

                suggestionRv.visibility = View.INVISIBLE
                hideKeyboard()
            }
        }


        postContent.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                postContent.error = null
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInput.error = null
                val input = s.toString().trim()
                if (input.isNotEmpty() && input.startsWith("#") ) {
                    suggestionRv.visibility = View.VISIBLE
                    showSuggestions(input)
                } else {
                    hideSuggestions()
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        postBtn.setOnClickListener {

            val content = postContent.text.toString()

            val hashtag = textInput.text.toString()

            if (!postViewModel.isValidContent(content)){

                postContent.error = "Post cannot be empty"

            }

            if (!postViewModel.isValidHashtag(hashtag)){
                if (textInput.visibility == View.VISIBLE){
                    textInput.error = "Choose a valid hashtag"
                }else {
                    toast(requireContext(), "Choose a valid hashtag")
                }
            }

            //TODO: IMPROVE THIS CODE
            if (postViewModel.isValidHashtag(hashtag) && postViewModel.isValidContent(content)){
                postViewModel.addPost(userId, content, hashtag)
                textInput.setText("")
                postContent.setText("")

            }

//            val post = com.example.anonifydemo.ui.dataClasses.Post(
//                uid = uid,
//                text = postContent.text.toString(),
//                hashtag = textInput.
//            )
        }
    }

    private fun showSuggestions(input: String) {
        postViewModel.generateSuggestions(input)
    }

    private fun hideSuggestions() {
        suggestionRv.visibility = View.GONE
    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding!!.textInput, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding!!.textInput.windowToken, 0)
    }
}

