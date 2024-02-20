package com.example.anonifydemo.ui.onboard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentOnboardBinding

class OnboardFragment : Fragment() {

    private var _binding : FragmentOnboardBinding? = null

    private val binding get() = _binding

    private lateinit var viewModel: OnboardViewModel

    private lateinit var getStarted : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOnboardBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getStarted = binding!!.getstart

        getStarted.setOnClickListener {

            if (findNavController().currentDestination!!.id == R.id.onboardFragment){
                findNavController().navigate(R.id.action_onboardFragment_to_login_frgament)
            }

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OnboardViewModel::class.java)
        // TODO: Use the ViewModel
    }

}