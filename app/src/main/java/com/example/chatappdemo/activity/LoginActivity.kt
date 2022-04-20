package com.example.chatappdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.chatappdemo.R
import com.example.chatappdemo.helper.KeyClass
import com.example.chatappdemo.helper.SessionManager
import com.example.chatappdemo.helper.Validation
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var txtSignup: TextView
    private lateinit var etEmailLogin: EditText
    private lateinit var etPassLogin: EditText
    private lateinit var btnLogin: Button
    private lateinit var progressLayout: RelativeLayout
    private lateinit var loginLayout: ConstraintLayout

    /*Variables used in managing the login session*/
    private lateinit var sessionManager: SessionManager

    // Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    // internet available Variable
    private val isNetworkAvailable: Boolean
        get() {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            return true
                        }
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                            return true
                        }
                    }
                }
            }
            return false
        }

    // internet Permission
    private fun internetPermission(){
        if (!isNetworkAvailable) {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Internet Connection Alert")
                .setMessage("Please Check Your Internet Connection")
                .setPositiveButton(
                    "Close"
                ) { dialog, _ -> dialog.dismiss() }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // For Disable Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar!!.hide() // Hide Toolbar
        // initialization of Views or variable
        initView()

        if (sessionManager.isLoggedIn()) {
            if (!isNetworkAvailable){
                internetPermission()
            }
            else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        txtSignup.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
        }

        btnLogin.setOnClickListener {
//            val i = Intent(this,MainActivity::class.java)
//            startActivity(i)

            val email = etEmailLogin.text.toString()
            val password = etPassLogin.text.toString()


            if(TextUtils.isEmpty(etEmailLogin.text.toString())) {
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.empty_email),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!Validation.isValidEmailFormat(etEmailLogin.text.toString())) {
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.email_valid),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (TextUtils.isEmpty(etPassLogin.text.toString())) {
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.empty_password),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else if (!Validation.isValidPasswordFormat(etPassLogin.text.toString())){
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.password_not_valid),
                    Toast.LENGTH_LONG
                ).show()
            }

            else {
                // internet Permission Check
                if (!isNetworkAvailable){
                    internetPermission()
                }
                else{
                    // Api Hit
                    btnClick()
                    login(email,password)
                }
            }

        }
    }

    private fun initView(){
        loginLayout = findViewById(R.id.loginLayout)
        txtSignup = findViewById(R.id.txtSignup)
        etEmailLogin = findViewById(R.id.etEmailLogin)
        etPassLogin = findViewById(R.id.etPassLogin)
        btnLogin = findViewById(R.id.btnLogin)
        progressLayout = findViewById(R.id.progressLayout)
        sessionManager = SessionManager(this@LoginActivity)

        mAuth = FirebaseAuth.getInstance()
    }

    // For current Focus
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun btnClick(){  // for button click
        btnLogin.isEnabled = false
        btnLogin.alpha = 0.7f
        progressLayout.visibility = View.VISIBLE
        loginLayout.alpha = 0.7f
        etEmailLogin.isEnabled = false
        etPassLogin.isEnabled = false
        txtSignup.isEnabled = false
    }
    private fun btnUnClick(){  // for button unClick
        btnLogin.isEnabled = true
        btnLogin.alpha = 1f
        progressLayout.visibility = View.GONE
        loginLayout.alpha = 1f
        txtSignup.isEnabled = true
        etEmailLogin.isEnabled = true
        etPassLogin.isEnabled = true
    }

    private fun login(email: String, password : String){

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val i = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(i)
                    sessionManager.putString(KeyClass.KEY_EMAIL,email)
                    sessionManager.putString(KeyClass.KEY_PASSWORD,password)
                    sessionManager.putBoolean(KeyClass.KEY_USER_LOGIN, true)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "User Doesn't exist.", Toast.LENGTH_SHORT).show()
                    btnUnClick()
                }
            }

    }

}