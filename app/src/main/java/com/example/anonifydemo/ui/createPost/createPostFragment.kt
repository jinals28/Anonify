package com.example.anonifydemo.ui.createPost

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentCreatePostBinding
import com.google.android.material.chip.Chip

class createPostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding?=null
    private val binding get() = _binding
    private lateinit var suggestionsAdapter: SuggestionsAdapter
//    private lateinit var hashtagChip: Chip
//    private lateinit var hashtagChip: textInputFie
//
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding=FragmentCreatePostBinding.inflate(layoutInflater, container, false)
//        return binding!!.root
//    }
//
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
}