    package com.example.anonifydemo.ui.ChooseAvatar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anonifydemo.R

class chooseAvatarFragment : Fragment() {

    companion object {
        fun newInstance() = chooseAvatarFragment()
    }

    private lateinit var viewModel: ChooseAvatarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_avatar, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseAvatarViewModel::class.java)
        // TODO: Use the ViewModel
    }

}