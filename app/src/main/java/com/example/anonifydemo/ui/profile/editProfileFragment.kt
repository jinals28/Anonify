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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentEditProfileBinding
import com.example.anonifydemo.databinding.FragmentOnboardBinding
import com.example.anonifydemo.ui.dataClasses.Avatar
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.home.HomeFragmentDirections
import de.hdodenhof.circleimageview.CircleImageView

class editProfileFragment : Fragment() {
    private var _binding : FragmentEditProfileBinding? = null
    private val binding get() = _binding

    private val userViewModel : UserViewModel by activityViewModels()

    private val viewModel : EditProfileViewModel by viewModels()

  //  private lateinit var editemail: EditText
    private lateinit var editbio : EditText
 //   private lateinit var lblemail:TextView
    private lateinit var lblbio:TextView
    private lateinit var btnback:ImageButton
    private lateinit var editavatar:TextView
    private lateinit var editimgusr : CircleImageView
    private lateinit var editusrnm: TextView
    private lateinit var btnedit:Button
    private var avatarId : Long = -1L

    private var avatar : Avatar = Avatar()

    private var userId : String= ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(layoutInflater, container, false)

        userId = userViewModel.getUserId()
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editbio = binding!!.txteditbio

        lblbio = binding!!.lblbio

        btnback = binding!!.btnback

        editavatar =binding!!.editavatar

        editimgusr = binding!!.editimgusr

        editusrnm = binding!!.editusrnm

        btnedit = binding!!.btnedit

        avatar = userViewModel.getUser()!!.avatar

        editimgusr.setImageDrawable(ContextCompat.getDrawable(requireContext(), avatar.url))

        editusrnm.text = avatar.name

        btnedit.setOnClickListener{
            //update profile
            val bio = editbio.text.toString()

            viewModel.updateBio(userId, bio){
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                goToProfileFragment()
            }


        }

        btnback.setOnClickListener {
            goToProfileFragment()
        }

//        editemail.setOnFocusChangeListener { _, hasFocus ->
//            handleFocusChange(hasFocus, editemail, lblemail)
//        }

        editbio.setOnFocusChangeListener { _, hasFocus ->
            handleFocusChange(hasFocus, editbio, lblbio)
        }
        editavatar.setOnClickListener{
            gotoavatarFragment()
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
        val navController = findNavController()
        if (findNavController().currentDestination!!.id == R.id.editProfileFragment) {
//            val action = editProfileFragmentDirections.actionEditProfileFragmentToNavigationProfile()
//            findNavController().navigate(action)
               navController.navigateUp()
        }
    }
    private fun  gotoavatarFragment() {
        if (findNavController().currentDestination!!.id == R.id.editProfileFragment) {
            val action = editProfileFragmentDirections.actionEditProfileFragmentToChooseAvatarFragment()
            findNavController().navigate(action)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

