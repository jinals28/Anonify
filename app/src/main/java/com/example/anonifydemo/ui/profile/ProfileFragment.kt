package com.example.anonifydemo.ui.profile

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.FragmentProfileBinding
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.home.postRecyclerView.PostRecyclerViewAdapter
import com.example.anonifydemo.ui.utils.AuthenticationUtil
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var postAdapter: PostRecyclerViewAdapter
    private lateinit var rv: RecyclerView
    private var _binding :FragmentProfileBinding?=null
    private val binding get() = _binding
    //private lateinit var viewModel: ProfileViewModel
    private val userViewModel : UserViewModel by activityViewModels()

    private val viewModel : ProfileViewModel by viewModels()

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
    private lateinit var shimmerrv: ShimmerFrameLayout
    private lateinit var btnsaved: Button
    private lateinit var txtnopost : LinearLayout
    //private lateinit var layoutfollowing: LinearLayout
    private var avatarId : Long = -1L

    private var avatar : Int = -1

    private var userId : String = ""

    private lateinit var user : ActiveUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding=FragmentProfileBinding.inflate(layoutInflater, container, false)
        userId = userViewModel.getUserId()
        lifecycleScope.launch {

            viewModel.getUser(userId)
        }
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
        txtnopost=binding!!.txtnopost

        rv = binding!!.rv

        shimmerrv = binding!!.shimmerrv

        //shimmerrv.startShimmer()

        postAdapter = PostRecyclerViewAdapter(requireContext(), userViewModel.getUserId())

        rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }

        viewModel.currentUser.observe(viewLifecycleOwner){ user ->

            avatar = user.avatar.url

            imgusr.setImageDrawable(ContextCompat.getDrawable(requireContext(), avatar))

            txtusrnm.text = user.avatar.name

            txtfollowing.text = user.followingTopicsCount.toString()

            txtpo.text = user.postCount.toString()

            txtpoints.text = user.advicePointCount.toString()

            txtbio.text = user.bio

        }

        viewModel.list.observe(viewLifecycleOwner){
            postAdapter.submitList(it.toMutableList())
            if (it.isEmpty()) {
                shimmerrv.stopShimmer()
                shimmerrv.visibility = View.GONE
                txtnopost.visibility = View.VISIBLE

            } else {
                   shimmerrv.stopShimmer()
                   shimmerrv.visibility = View.GONE
                    txtnopost.visibility = View.GONE
                   rv.visibility = View.VISIBLE
            }


        }

        btnpost.setOnClickListener {
            viewModel.getPost()
        }

        btnsaved.setOnClickListener {
            viewModel.getSavedPost()
        }

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
        val contactMe = bottomview.findViewById<LinearLayout>(R.id.layoutContact)
        val deleteUser = bottomview.findViewById<LinearLayout>(R.id.layoutDelete)
        val logOut = bottomview.findViewById<LinearLayout>(R.id.layoutLogOut)

        item1.setOnClickListener {
            Toast.makeText(requireContext(), "notification", Toast.LENGTH_SHORT).show()
           // dialog.dismiss()
        }
        item2.setOnClickListener {
            Toast.makeText(requireContext(), "hashtag management", Toast.LENGTH_SHORT).show()
          //  dialog.dismiss()
        }
        contactMe.setOnClickListener {
            Toast.makeText(requireContext(), "contact us", Toast.LENGTH_SHORT).show()
            contactUs()
           // dialog.dismiss()
        }
        deleteUser.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setTitle("Confirm Deletion")
            alertDialogBuilder.setMessage("Are you sure you want to delete your account?")
            alertDialogBuilder.setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteUser(userId){
                    Toast.makeText(requireContext(), "Deleting account...", Toast.LENGTH_SHORT).show()
                    goToSignInFragment()
                    removeActiveUser()
                }
                dialog.dismiss()
            }
            alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
            dialog.dismiss()
        }
        logOut.setOnClickListener {
                    //logout code
                removeActiveUser()
                AuthenticationUtil.logout(requireContext())
//                AuthenticationUtil.clearRememberMe(requireContext())
                dialog.dismiss()
                goToSignInFragment()
                 // Dismiss the dialog if needed
        }
        dialog.show()

        dialog.window?.setLayout (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color. TRANSPARENT))
        dialog.window?.setGravity (Gravity. BOTTOM)
    }

    private fun removeActiveUser() {
        val sharedPreferences = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    private fun goToSignInFragment() {
        if (findNavController().currentDestination!!.id == R.id.navigation_profile) {
            val action = ProfileFragmentDirections.actionNavigationProfileToSignInFragment()
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