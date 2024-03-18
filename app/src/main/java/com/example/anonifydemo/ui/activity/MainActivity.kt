package com.example.anonifydemo.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
//import androidx.credentials.CredentialManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ActivityMainBinding
import com.example.anonifydemo.ui.dataClasses.UserViewModel
import com.example.anonifydemo.ui.repository.AppRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding

    private val userViewModel : UserViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var navController: NavController

    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Thread.sleep(1000)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        lifecycleScope.launch {
            AppRepository.getTopics()
        }


//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
//            insets.consumeSystemWindowInsets()
//        }
    }

    override fun onStart() {
        super.onStart()

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        bottomNavigationView = binding.navView

        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.itemIconTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.btn))

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
                R.id.editProfileFragment->{
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