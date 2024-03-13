package com.example.anonifydemo.ui.homeScreen

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseTopicBinding
import com.example.anonifydemo.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    private var _binding : FragmentFeedBinding? = null

    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentFeedBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}