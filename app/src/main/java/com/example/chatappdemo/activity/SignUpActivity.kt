package com.example.chatappdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.UserHandle
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
import com.example.chatappdemo.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var txtLogin: TextView
    private lateinit var btnSignup: Button
    private lateinit var etNameSignUp: EditText
    private lateinit var etEmailSignup: EditText
    private lateinit var etPassSignup: EditText
    private lateinit var progressLayout: RelativeLayout
    private lateinit var signupLayout: ConstraintLayout

    /*Variables used in managing the login session*/
    private lateinit var sessionManager: SessionManager

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference

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

    private fun initView(){
        txtLogin = findViewById(R.id.txtLogin)
        etNameSignUp = findViewById(R.id.etNameSignup)
        etEmailSignup = findViewById(R.id.etEmailSignup)
        etPassSignup = findViewById(R.id.etPassSignup)
        btnSignup = findViewById(R.id.btnSignup)
        progressLayout = findViewById(R.id.progressLayout)
        signupLayout = findViewById(R.id.signupLayout)
        sessionManager = SessionManager(this@SignUpActivity)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // For Disable Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar!!.hide() // Hide Toolbar

        // initialization of view and variables
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

        txtLogin.setOnClickListener {
            onBackPressed()
        }

        btnSignup.setOnClickListener {
            val name = etNameSignUp.text.toString()
            val email = etEmailSignup.text.toString()
            val password = etPassSignup.text.toString()

            if (TextUtils.isEmpty(etNameSignUp.text.toString())) {
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.empty_Name),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (Validation.isValidSpaceFormat(etNameSignUp.text.toString())){
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.Name_space),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!Validation.isValidLatterFormat(etNameSignUp.text.toString())) {
//            else if (!Validation.isValidNameFormat(etFName.text.toString())) {

                Toast.makeText(
                    this,
                    this.resources.getString(R.string.Name_valid),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if(TextUtils.isEmpty(etEmailSignup.text.toString())) {
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.empty_email),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (!Validation.isValidEmailFormat(etEmailSignup.text.toString())) {
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.email_valid),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (TextUtils.isEmpty(etPassSignup.text.toString())) {
                Toast.makeText(
                    this,
                    this.resources.getString(R.string.empty_password),
                    Toast.LENGTH_SHORT
                ).show()
            }

            else if (!Validation.isValidPasswordFormat(etPassSignup.text.toString())){
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
                    signUp(name,email,password)
                }
            }

        }
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

    private fun btnClick(){    // button click
        btnSignup.isEnabled = false
        btnSignup.alpha = 0.7f
        setAllFieldDisable()
    }
    private fun btnUnClick(){   // button unClick
        btnSignup.isEnabled = true
        btnSignup.alpha = 1f
        setAllFieldEnable()
    }

    private fun setAllFieldEnable(){
        progressLayout.visibility = View.GONE
        signupLayout.alpha = 1f
        etEmailSignup.isEnabled = true
        etPassSignup.isEnabled = true
        etNameSignUp.isEnabled = true
        txtLogin.isEnabled = true
    }

    private fun setAllFieldDisable(){
        progressLayout.visibility = View.VISIBLE
        signupLayout.alpha = 0.7f
        etEmailSignup.isEnabled = false
        etPassSignup.isEnabled = false
        etNameSignUp.isEnabled = false
        txtLogin.isEnabled = false
    }

    private fun signUp(name: String,email: String, password : String){
        // logic of creating user
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val i = Intent(this@SignUpActivity,MainActivity::class.java)
                    startActivity(i)
                    addUserToDatabase(name,email, mAuth.currentUser!!.uid)
                    sessionManager.putString(KeyClass.KEY_EMAIL,email)
                    sessionManager.putString(KeyClass.KEY_PASSWORD,password)
                    sessionManager.putBoolean(KeyClass.KEY_USER_LOGIN, true)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@SignUpActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    btnUnClick()
                }
            }
    }

    private fun addUserToDatabase(name: String,email: String,uid: String){

        mDbRef.child(KeyClass.KEY_USER).child(uid).setValue(User(name,email,uid))

    }


}