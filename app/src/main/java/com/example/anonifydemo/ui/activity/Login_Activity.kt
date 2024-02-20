package com.example.anonifydemo.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.anonifydemo.R

class Login_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        /*loginbtn=findViewById(R.id.loginbtn)
        loginbtn.setOnClickListener {
          val intent = Intent(this,signupActivity::class.java)
            finish()
            startActivity(intent)
      }*/
    }
}