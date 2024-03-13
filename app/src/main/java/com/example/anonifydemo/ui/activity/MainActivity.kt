package com.example.anonifydemo.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.credentials.CredentialManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.anonifydemo.R
import com.example.anonifydemo.databinding.ActivityMainBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding

    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Thread.sleep(1000)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        WindowCompat.setDecorFitsSystemWindows(window, false)
//        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets ->
//            insets.consumeSystemWindowInsets()
//        }
    }

    override fun onStart() {
        super.onStart()

//       if (auth.currentUser != null){
//           Log
//            findNavController(R.id.nav_host_fragment_activity_main).navigate(R.id.action_onboardFragment_to_chooseAvatarFragment)
//       }


    }

}