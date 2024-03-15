package com.example.anonifydemo.ui.chooseAvatar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseAvatarBinding
import com.example.anonifydemo.ui.chooseAvatar.recyclerview.AvatarRecyclerViewAdapter
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.dataClasses.User
import com.example.anonifydemo.ui.dataClasses.UserViewModel

class ChooseAvatarFragment : Fragment() {

    private var _binding : FragmentChooseAvatarBinding? = null

    private val userViewModel : UserViewModel by activityViewModels()
    private val binding get() = _binding

    //private lateinit var viewModel: ChooseAvatarViewModel
    private lateinit var btnavatar: Button

    private lateinit var avatarRv : RecyclerView

    private lateinit var user : User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseAvatarBinding.inflate(layoutInflater, container, false)

        return binding!!.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            avatarRv = binding!!.avatarRv



            val imageId = listOf<Avatar>(
                Avatar(R.drawable.dinosaur,"Moki"),
                Avatar(R.drawable.dog,"Jinto"),
                Avatar(R.drawable.panda,"Yarri"),
                Avatar(R.drawable.rabbit,"Zink"),
                Avatar(R.drawable.bear,"Loki"),
                Avatar(R.drawable.cat,"Yolo"),
                Avatar(R.drawable.octopus,"Kairo"),
                Avatar(R.drawable.owl,"Lumi"),
                Avatar(R.drawable.deer,"Yara"),
                Avatar(R.drawable.tiger,"Lokai"),
                Avatar(R.drawable.shark,"Soli"),
                Avatar(R.drawable.elephant,"Juno"),
                Avatar(R.drawable.lion,"Simba"),
                Avatar(R.drawable.wolf,"Jinx"),
                Avatar(R.drawable.sloth,"Zinna"),
                Avatar(R.drawable.rabbit2,"Lexa"),
                Avatar(R.drawable.llama,"Lyric"),
                Avatar(R.drawable.penguin,"Zolar"))

            val adapter = AvatarRecyclerViewAdapter(requireContext(), imageId, userViewModel)

            avatarRv.adapter = adapter



        }
    }