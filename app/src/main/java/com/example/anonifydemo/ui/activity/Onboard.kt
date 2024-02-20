package com.example.anonifydemo.ui.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.anonifydemo.R

class onboard : AppCompatActivity() {

lateinit var getstart:Button
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        installSplashScreen()
        Thread.sleep(5000)
        setContentView(R.layout.activity_onboard)

        //transparent code

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT

        getstart=findViewById(R.id.getstart)
        getstart.setOnClickListener {
            val intent = Intent(this@onboard, Login_Activity::class.java)
            finish()
            startActivity(intent)
        }
    }
}