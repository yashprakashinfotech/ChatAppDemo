package com.example.chatappdemo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.chatappdemo.R

class LoginActivity : AppCompatActivity() {

    private lateinit var txtSignup: TextView
    private lateinit var etEmailLogin: EditText
    private lateinit var etPassLogin: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // For Disable Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // initialization of Views or variable
        initView()

        txtSignup.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }

        btnLogin.setOnClickListener {
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
    }

    private fun initView(){
        txtSignup = findViewById(R.id.txtSignup)
        etEmailLogin = findViewById(R.id.etEmailLogin)
        etPassLogin = findViewById(R.id.etPassLogin)
        btnLogin = findViewById(R.id.btnLogin)
    }
}