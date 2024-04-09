package com.example.anonifydemo.ui.profile

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentProfileBinding
import com.example.anonifydemo.ui.dataClasses.UserViewModel
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
    private lateinit var txtbio: TextView
    private lateinit var txtfollowing: TextView
    private lateinit var txtpo: TextView
    private lateinit var txtpoints: TextView
    private lateinit var btnpost: Button
    private lateinit var btnsaved: Button
    private lateinit var layoutfollowing: LinearLayout
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
        txtbio = binding!!.txtbio
        txtfollowing = binding!!.txtfollowing
        txtpo = binding!!.txtpo
        txtpoints = binding!!.txtpoints
        btnpost = binding!!.btnpost
        btnsaved = binding!!.btnsaved
        layoutfollowing = binding!!.layoutfollowing

        avatar = userViewModel.getUser()!!.avatar.url
        imgusr.setImageDrawable(ContextCompat.getDrawable(requireContext(), avatar))

        editprofile.setOnClickListener {
            //function for edit profile fragment
            goToEditProfileFragment()
        }
        btnsettings.setOnClickListener{
            showBottomSheetDialog()
        }
        layoutfollowing.setOnClickListener{
            goToTopics()
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

        item1.setOnClickListener {
            Toast.makeText(requireContext(), "notification", Toast.LENGTH_SHORT).show()
           // dialog.dismiss()
        }
        item2.setOnClickListener {
            Toast.makeText(requireContext(), "hashtag management", Toast.LENGTH_SHORT).show()
          //  dialog.dismiss()
        }
        item3.setOnClickListener {
            Toast.makeText(requireContext(), "contact us", Toast.LENGTH_SHORT).show()
            contactUs()
           // dialog.dismiss()
        }
        item4.setOnClickListener {
            Toast.makeText(requireContext(), "delete account", Toast.LENGTH_SHORT).show()
          //  dialog.dismiss()
        }
            item5.setOnClickListener {
                    //logout code
                AuthenticationUtil.logout(requireContext())
                AuthenticationUtil.clearRememberMe(requireContext())
                dialog.dismiss()
                goToSignInFragment()
                 // Dismiss the dialog if needed
            }
        dialog.show()

        dialog.window?.setLayout (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color. TRANSPARENT))
        dialog.window?.setGravity (Gravity. BOTTOM)
    }
    private fun goToSignInFragment() {
        if (findNavController().currentDestination!!.id == R.id.navigation_profile) {
            val action = ProfileFragmentDirections.actionNavigationProfileToSignInFragment()
            findNavController().navigate(action)
        }
    }
    private fun goToTopics() {
        if (findNavController().currentDestination!!.id == R.id.navigation_profile) {
            val action = ProfileFragmentDirections.actionNavigationProfileToChooseTopic()
            findNavController().navigate(action)
        }
    }
    private fun contactUs(){
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "sumansdjic@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback or Issue Report")
            intent.putExtra(Intent.EXTRA_TEXT, "Please describe your feedback or issue here about our app.")
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "gmail app not found", Toast.LENGTH_SHORT).show()
        }
    }
//        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
//            data = Uri.parse("mailto:sumansdjic@gmail.com")
//            putExtra(Intent.EXTRA_SUBJECT, "Feedback or Issue Report")
//            putExtra(Intent.EXTRA_TEXT, "Please describe your feedback or issue here about our app.")
//        }
//
//        if (emailIntent.resolveActivity(requireActivity().packageManager) != null) {
//            startActivity(emailIntent)
//        } else {
//            // Handle case where no email app is available
//            Toast.makeText(requireContext(), "gmail app not found", Toast.LENGTH_SHORT).show()
//        }
//    }
}