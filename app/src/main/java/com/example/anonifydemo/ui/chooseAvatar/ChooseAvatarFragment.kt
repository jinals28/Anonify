package com.example.anonifydemo.ui.chooseAvatar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentChooseAvatarBinding
import com.example.anonifydemo.ui.chooseAvatar.recyclerview.AvatarRecyclerViewAdapter

class ChooseAvatarFragment : Fragment() {

    private var _binding : FragmentChooseAvatarBinding? = null

    private val binding get() = _binding

    //private lateinit var viewModel: ChooseAvatarViewModel
    private lateinit var btnavatar: Button

    private lateinit var avatarRv : RecyclerView
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

            val imageId = listOf<Pair<Int, String>>(
                Pair(R.drawable.dinosaur,"Moki"),
                    Pair(R.drawable.dog,"Jinto"),
                    Pair(R.drawable.panda,"Yarri"),
                    Pair(R.drawable.rabbit,"Zink"),
                    Pair(R.drawable.bear,"Loki"),
                    Pair(R.drawable.cat,"Yolo"))
//

            val imageUrls = resources.getIntArray(R.array.image_id).toList()

            val adapter = AvatarRecyclerViewAdapter(requireContext(), imageId)

            avatarRv.adapter = adapter

//            btnavatar = binding!!.btnavatar
//            btnavatar.setOnClickListener{
//                if(findNavController().currentDestination!!.id==R.id.chooseAvatarFragment){
//                    findNavController().navigate(R.id.action_chooseAvatarFragment_to_chooseTopic)
//
//                }
//            }
        }
    }