package com.example.anonifydemo.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentEditProfileBinding
import com.example.anonifydemo.databinding.FragmentOnboardBinding
import com.example.anonifydemo.ui.home.HomeFragmentDirections

class editProfileFragment : Fragment() {
    private var _binding : FragmentEditProfileBinding? = null
    private val binding get() = _binding
    private lateinit var editemail: EditText
    private lateinit var editbio : EditText
    private lateinit var lblemail:TextView
    private lateinit var lblbio:TextView
    private lateinit var btnback:ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editemail = binding!!.txteditemail
        editbio = binding!!.txteditbio
        lblemail = binding!!.lblemail
        lblbio = binding!!.lblbio
        btnback = binding!!.btnback

        btnback.setOnClickListener {
            goToProfileFragment()
        }
        editemail.setOnFocusChangeListener { _, hasFocus ->
            handleFocusChange(hasFocus, editemail, lblemail)
        }

        editbio.setOnFocusChangeListener { _, hasFocus ->
            handleFocusChange(hasFocus, editbio, lblbio)
        }
    }
    private fun handleFocusChange(hasFocus: Boolean, editText: EditText, labelTextView: TextView) {
        //added a small animation for hint to label transition
        if (hasFocus) {
            labelTextView.visibility = View.VISIBLE
            labelTextView.alpha = 0f
            labelTextView.animate()
                .alpha(1f)
                .setDuration(200)
                .start()
            editText.hint = ""
        } else {
            if (editText.text.isEmpty()) {
                labelTextView.visibility = View.INVISIBLE
                editText.hint = labelTextView.text
            }
        }
    }
    private fun  goToProfileFragment() {
        if (findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            val action = editProfileFragmentDirections.actionEditProfileFragmentToNavigationProfile()
            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

