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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCreatePostBinding
import com.example.anonifydemo.ui.dataClasses.Post
import com.google.android.material.chip.Chip
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.utils.Utils
import de.hdodenhof.circleimageview.CircleImageView

class CreatePostFragment : Fragment(), Utils {

    private var _binding: FragmentCreatePostBinding?=null
    private val binding get() = _binding

    private val userViewModel : UserViewModel by activityViewModels()

    private lateinit var postViewModel : CreatePostViewModel

    private lateinit var userAvatar : CircleImageView

    private lateinit var suggestionList : List<String>

    private lateinit var suggestionRv : RecyclerView

   private lateinit var hashtagChip: Chip

    private lateinit var textInput: EditText

    private lateinit var postContent : EditText

    private lateinit var postBtn : Button

    private lateinit var uid : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentCreatePostBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel = ViewModelProvider(this).get(CreatePostViewModel::class.java)

        suggestionList = resources.getStringArray(R.array.topic_names).toList()

        postViewModel.set(suggestionList)

        suggestionRv = binding!!.suggestionsRecyclerView

        hashtagChip = binding!!.hashtagChip

        textInput = binding!!.textInput

        postContent = binding!!.postContent

        userAvatar = binding!!.imgUsr

        postBtn = binding!!.toolbarButtonPost

        uid = userViewModel.getUser()!!.uid

        postViewModel.topicList.observe(viewLifecycleOwner){ suggestionsList ->
            val suggestionsAdapter = SuggestionsAdapter(suggestionsList) { suggestionItem ->
                binding!!.textInput.setText("$suggestionItem")
                hashtagChip.text = suggestionItem
                hideSuggestions()
            }
            suggestionRv.adapter = suggestionsAdapter
        }


        userViewModel.user.observe(viewLifecycleOwner){
            userAvatar.setImageDrawable(ContextCompat.getDrawable(requireContext(), it.avatarUrl.id))
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

                postContent.error = "Post should contain words"

            }

            if (!postViewModel.isValidHashtag(hashtag)){
                if (textInput.visibility == View.VISIBLE){
                    textInput.error = "Choose a valid hashtag"
                }else {
                    toast(requireContext(), "Choose a valid hashtag")
                }

            }

            if (postViewModel.isValidHashtag(hashtag) && postViewModel.isValidContent(content)){
                postViewModel.addPost(Post(
                    uid = uid,
                    text = content,
                    hashtag = hashtag
                ))
                textInput.setText("")
                postContent.setText("")

            }




//            val post = Post(
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

