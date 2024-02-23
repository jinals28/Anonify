package com.example.anonifydemo.ui.ChooseTopics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentAddTopicBinding
import com.example.anonifydemo.databinding.FragmentLoginBinding

class fragment_add_topic : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding : FragmentAddTopicBinding? = null

    private val binding get() = _binding

//    private lateinit var viewModel: LoginViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_topic, container, false)
    }


}