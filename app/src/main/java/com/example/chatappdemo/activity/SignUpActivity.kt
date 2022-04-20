package com.example.chatappdemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.example.chatappdemo.R

class SignUpActivity : AppCompatActivity() {

    private lateinit var txtLogin: TextView
    private lateinit var btnSignup: Button
    private lateinit var etEmailSignup: EditText
    private lateinit var etPassSignup: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // For Disable Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // initialization of view and variables
        initView()

        txtLogin.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initView(){
        txtLogin = findViewById(R.id.txtLogin)
        etEmailSignup = findViewById(R.id.etEmailSignup)
        etPassSignup = findViewById(R.id.etPassSignup)
        btnSignup = findViewById(R.id.btnSignup)
    }
}