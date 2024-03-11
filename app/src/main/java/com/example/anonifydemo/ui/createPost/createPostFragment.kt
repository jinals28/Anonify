package com.example.anonifydemo.ui.createPost

import android.content.Context
import androidx.lifecycle.ViewModelProvider
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCreatePostBinding
import com.google.android.material.chip.Chip
import com.example.anonifydemo.ui.createPost.SuggestionItem

class createPostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding?=null
    private val binding get() = _binding
    private lateinit var suggestionsAdapter: SuggestionsAdapter
   private lateinit var hashtagChip: Chip
    private lateinit var textinput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentCreatePostBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hashtagChip = binding!!.hashtagChip
        val textInput = binding!!.textInput
        setupSuggestionsRecyclerView()

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
                hideKeyboard()
            }
        }

        textInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if (input.isNotEmpty() && input.startsWith("#")) {
                    showSuggestions(input.substring(1))
                } else {
                    hideSuggestions()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupSuggestionsRecyclerView() {
        suggestionsAdapter = SuggestionsAdapter(emptyList()) { suggestionItem ->
            binding!!.textInput.setText("#${suggestionItem.text}")
            hideSuggestions()
        }
        binding!!.suggestionsRecyclerView.adapter = suggestionsAdapter
        binding!!.suggestionsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showSuggestions(input: String) {
        val suggestions = generateSuggestions(input)
        suggestionsAdapter = SuggestionsAdapter(suggestions) { suggestionItem ->
            binding!!.textInput.setText("#${suggestionItem.text}")
            hideSuggestions()
        }
        binding!!.suggestionsRecyclerView.adapter = suggestionsAdapter
        binding!!.suggestionsRecyclerView.visibility = View.VISIBLE
    }

    private fun hideSuggestions() {
        binding!!.suggestionsRecyclerView.visibility = View.GONE
    }

    private fun generateSuggestions(input: String): List<SuggestionItem> {
        // Generate suggestions based on the input
        // You can fetch suggestions from a database or use a hardcoded list
        return listOf(
            SuggestionItem("suggestion1"),
            SuggestionItem("suggestion2"),
            SuggestionItem("suggestion3")
        ).filter { it.text.startsWith(input, ignoreCase = true) }
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
