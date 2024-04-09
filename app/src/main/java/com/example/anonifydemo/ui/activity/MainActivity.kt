package com.example.anonifydemo.ui.activity

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
//import androidx.credentials.CredentialManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ActivityMainBinding
import com.example.anonifydemo.ui.dataClasses.ActiveUser
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.repository.AppRepository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding

    val userViewModel : UserViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var navController: NavController

    private var auth = Firebase.auth

    private var user : ActiveUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Thread.sleep(1000)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        removeActiveUser()

//        if (user!= null){
//            lifecycleScope.launch{
//                AppRepository.getFollowingTopicsForUser(user!!.uid)
//            }
//        }

    }

    fun removeActiveUser() {
        val sharedPreferences = this.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    private fun getActiveUser(): String {
        val sharedPreferences = this.getSharedPreferences("user_data", Context.MODE_PRIVATE)
        return sharedPreferences.getString("uid", null)
        //            val email = sharedPreferences.getString("email", "")
        //            val createdAt = sharedPreferences.getLong("createdAt", 0)
        //            val avatarName = sharedPreferences.getString("avatarName", "")
        //            val avatarUrl = sharedPreferences.getInt("avatarUrl", 0)
        //            val followingTopicsSet = sharedPreferences.getStringSet("followingTopics", setOf()) ?: setOf()
        //            val followingTopicsList = followingTopicsSet.map { FollowingTopic(it, -1) }
        //            val followingTopicsCount = sharedPreferences.getLong("followingTopicsCount", 0)
        //            ActiveUser(uid = uid,
        //                email = email!!,
        //                createdAt = createdAt!!,
        //                avatar = Avatar(url = avatarUrl, name = avatarName!!), followingTopics = followingTopicsList, followingTopicsCount = followingTopicsCount)
            ?: ""
    }

    suspend fun loadHome(userId : String) {
        user = AppRepository.getUser(userId = userId)
        userViewModel.setUser(user!!)
    }


    override fun onStart() {
        super.onStart()

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        bottomNavigationView = binding.navView

        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.itemIconTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn))
        //remember me
//        if (AuthenticationUtil.isLoggedIn(this)) {
//            navController.navigate(R.id.navigation_home)
//        }


        val userId = getActiveUser()

        if (userId != "") {
            Log.d("Anonify User", user.toString())
            lifecycleScope.launch {
                loadHome(userId)
            }
            navController.navigate(R.id.navigation_home)
        }

//        bottomNavigationView.itemIconTintList = getColorStateList(R.color.bottom_navigation_icon_selector)
        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){

                R.id.onboardFragment,
                R.id.loginFragment,
                R.id.signInFragment,
                R.id.chooseAvatarFragment,
                R.id.chooseTopic,
                R.id.signUpFragment,
                R.id.commentFragment,
                R.id.searchCommunityFragment,
                R.id.createCommunityFragment,
                R.id.communityProfileFragment,
                R.id.editProfileFragment,
                R.id.profile2Fragment->{
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }

            }

        }

//       if (auth.currentUser != null){
//           Log
//            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_onboardFragment_to_chooseAvatarFragment)
//       }
    }
}