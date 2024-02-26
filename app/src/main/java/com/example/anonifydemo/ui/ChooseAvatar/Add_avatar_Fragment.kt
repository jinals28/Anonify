package com.example.anonifydemo.ui.ChooseAvatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentAddAvatarBinding
import com.example.anonifydemo.databinding.FragmentChooseTopicBinding

/**
 * A simple [Fragment] subclass.
 * Use the [Add_avatar_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Add_avatar_Fragment : Fragment() {
    private var _binding : FragmentAddAvatarBinding? = null

    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentAddAvatarBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}