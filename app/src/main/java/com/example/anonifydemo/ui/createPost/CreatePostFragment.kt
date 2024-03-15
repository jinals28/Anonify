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
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCreatePostBinding
import com.google.android.material.chip.Chip
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import de.hdodenhof.circleimageview.CircleImageView

class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding?=null
    private val binding get() = _binding

    private val userViewModel : UserViewModel by activityViewModels()

    private lateinit var postViewModel : CreatePostViewModel


    private lateinit var userAvatar : CircleImageView

    private lateinit var suggestionList : List<String>

    private lateinit var suggestionRv : RecyclerView

   private lateinit var hashtagChip: Chip

    private lateinit var textInput: EditText

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

        userAvatar = binding!!.imgUsr

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
        textInput.setText("#")
        textInput.setSelection(1) // Set the cursor after the '#'
//        setupSuggestionsRecyclerView()

        hashtagChip.setOnClickListener {
            hashtagChip.visibility = View.GONE
            textInput.visibility = View.VISIBLE
            textInput.requestFocus()
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

        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if (input.isNotEmpty() && input.startsWith("#") ) {
                    suggestionRv.visibility = View.VISIBLE
                    showSuggestions(input)
                } else {
                    Toast.makeText(requireContext(), "hide", Toast.LENGTH_SHORT).show()
                    hideSuggestions()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

//    private fun setupSuggestionsRecyclerView() {
//
//        suggestionsAdapter = SuggestionsAdapter(suggestionsList) { suggestionItem ->
//            binding!!.textInput.setText("#$suggestionItem")
//            hideSuggestions()
//        }
//        suggestionRv.adapter = suggestionsAdapter
//
//    }

    private fun showSuggestions(input: String) {
//
        postViewModel.generateSuggestions(input)
////        suggestionsAdapter = SuggestionsAdapter(suggestions) { suggestionItem ->
////            binding!!.textInput.setText("#$suggestionItem")
////            hideSuggestions()
////        }
//        binding!!.suggestionsRecyclerView.adapter = suggestionsAdapter
//        binding!!.suggestionsRecyclerView.visibility = View.VISIBLE
    }

    private fun hideSuggestions() {
        suggestionRv.visibility = View.GONE
    }

//    private fun generateSuggestions(input: String): List<String> {
//        // Generate suggestions based on the input
//        // You can fetch suggestions from a database or use a hardcoded list
//        return resources.getStringArray(R.array.topic_names).toList().filter { it.startsWith(input, ignoreCase = true) }
//    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding!!.textInput, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding!!.textInput.windowToken, 0)
    }
}

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val hashtagChip = binding.hashtagChip
//        val textInputField = binding.textInputField
//
//        // Set up the text input field to show the keyboard and request focus when the hashtag chip is clicked
//        hashtagChip.setOnClickListener {
//            textInputField.requestFocus()
//            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.showSoftInput(textInputField, InputMethodManager.SHOW_IMPLICIT)
//        }
//
//        // Create a list of suggestions
//        val suggestions = listOf(
//            SuggestionItem(1, "suggestion1"),
//            SuggestionItem(2, "suggestion2"),
//            SuggestionItem(3, "suggestion3"),
//            // Add more suggestions here
//        )
//
//        // Create the adapter and set it for the RecyclerView
//        suggestionsAdapter = SuggestionsAdapter(suggestions, ::onSuggestionItemClicked)
//        binding.suggestionsRecyclerView.adapter = suggestionsAdapter
//
//        // Set up the text input field to filter the suggestions as the user types
//        textInputField.addTextChangedListener {
//            // Filter the suggestions based on the entered text
//            val filteredSuggestions = suggestions.filter { suggestion ->
//                suggestion.text.contains(textInputField.text.toString(), ignoreCase = true)
//            }
//            suggestionsAdapter.updateItems(filteredSuggestions)
//        }
//    }
//
//    private fun onSuggestionItemClicked(suggestionItem: SuggestionItem) {
//        // Update the text input field with the selected suggestion
//        binding.textInputField.setText(suggestionItem.text)
//
//        // Hide the suggestions RecyclerView
//        binding.suggestionsRecyclerView.visibility = View.GONE
//    }
//
//
