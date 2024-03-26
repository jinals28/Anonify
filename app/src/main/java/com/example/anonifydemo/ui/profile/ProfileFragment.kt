package com.example.anonifydemo.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentProfileBinding
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.signin.SignInFragmentDirections
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private var _binding :FragmentProfileBinding?=null
    private val binding get() = _binding
    //private lateinit var viewModel: ProfileViewModel
    private val userViewModel : UserViewModel by activityViewModels()
    private lateinit var editprofile: Button
    private lateinit var btnsettings: ImageButton
    private lateinit var authUtil: AuthenticationUtil
    private lateinit var imgusr : CircleImageView
    private lateinit var txtusrnm: TextView
    private var avatarId : Long = -1L

    private var avatar : Int = -1

    private var userId : Long = -1L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editprofile = binding!!.btnEdit
        btnsettings = binding!!.btnsettings
        imgusr = binding!!.imgusr
        txtusrnm = binding!!.txtusrnm

        avatar = userViewModel.getUser()!!.avatar.url
        imgusr.setImageDrawable(ContextCompat.getDrawable(requireContext(), avatar))

        editprofile.setOnClickListener {
            //function for edit profile fragment
            goToEditProfileFragment()
        }
        btnsettings.setOnClickListener{
            showBottomSheetDialog()
        }
    }
    private fun goToEditProfileFragment() {
        if (findNavController().currentDestination!!.id == R.id.navigation_profile) {
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment()
            findNavController().navigate(action)
        }
    }
    private fun showBottomSheetDialog(){
        val bottomview =layoutInflater.inflate(R.layout.settings_bottomsheet,null)
        val dialog =BottomSheetDialog(requireContext())
        dialog.setContentView(bottomview)
        dialog.setCanceledOnTouchOutside(true)

        val item1 = bottomview.findViewById<LinearLayout>(R.id.layoutnotiication)
        val item2 = bottomview.findViewById<LinearLayout>(R.id.layoutHashMng)
        val item3 = bottomview.findViewById<LinearLayout>(R.id.layoutContact)
        val item4 = bottomview.findViewById<LinearLayout>(R.id.layoutDelete)
        val item5 = bottomview.findViewById<LinearLayout>(R.id.layoutLogOut)

        // Set OnClickListener for each item
        item1.setOnClickListener {
            // Handle click on item 1
            Toast.makeText(requireContext(), "notification", Toast.LENGTH_SHORT).show()
           // dialog.dismiss() // Dismiss the dialog if needed
        }
        item2.setOnClickListener {
            // Handle click on item 1
            Toast.makeText(requireContext(), "hashtag management", Toast.LENGTH_SHORT).show()
          //  dialog.dismiss() // Dismiss the dialog if needed
        }
        item3.setOnClickListener {
            // Handle click on item 1
            Toast.makeText(requireContext(), "contacct us", Toast.LENGTH_SHORT).show()
           // dialog.dismiss() // Dismiss the dialog if needed
        }
        item4.setOnClickListener {
            // Handle click on item 1
            Toast.makeText(requireContext(), "delete account", Toast.LENGTH_SHORT).show()
          //  dialog.dismiss() // Dismiss the dialog if needed
        }
            item5.setOnClickListener {
                // Handle click on item 1
                    //logout code
                authUtil.logout()
                goToSignInFragment()
                dialog.dismiss() // Dismiss the dialog if needed
            }
        dialog.show();

        dialog.window?.setLayout (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color. TRANSPARENT))
       // dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity (Gravity. BOTTOM)
    }
    private fun goToSignInFragment() {
        if (findNavController().currentDestination!!.id == R.id.navigation_profile) {
            val action = ProfileFragmentDirections.actionNavigationProfileToSignInFragment()
            findNavController().navigate(action)
        }
    }
}