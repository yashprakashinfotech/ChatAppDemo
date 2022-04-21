package com.example.chatappdemo.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappdemo.R
import com.example.chatappdemo.adapter.UserAdapter
import com.example.chatappdemo.helper.KeyClass
import com.example.chatappdemo.helper.SessionManager
import com.example.chatappdemo.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {


    private lateinit var sessionManager: SessionManager
    private lateinit var sharedPrefs : SharedPreferences

//    private lateinit var crash : Button

    // Database
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    private lateinit var userAdapter : UserAdapter
    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList : ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // For Disable Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initView()

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = userAdapter

        mDbRef.child(KeyClass.KEY_USER).addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()

                // For Get user list from Db to userList Array
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    // for check current user LoggedIn
                    if (mAuth.currentUser!!.uid != currentUser!!.uId){
                        userList.add(currentUser)
                    }

                }

                userAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {}

        })

//        crash.setOnClickListener {
//            throw RuntimeException("Test Crash") // Force a crash
//        }

    }

    private fun initView(){
        sessionManager = SessionManager(this)
        sharedPrefs= this.getSharedPreferences(sessionManager.prefFile,sessionManager.privateMode)
        userRecyclerView = findViewById(R.id.userRecyclerView)
//        crash = findViewById(R.id.crash)
        userList = ArrayList()
        userAdapter = UserAdapter(this,userList)
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
    }

    // Menu Item
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //Log out option
        logoutDialog()

        return super.onOptionsItemSelected(item)
    }

    private fun logoutDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
            .setMessage("Are you sure you want Log Out?")
            .setPositiveButton("Yes"){_,_ ->
                sessionManager.putBoolean(KeyClass.KEY_USER_LOGIN,false)
                sharedPrefs.edit().clear().apply()
                mAuth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
//                this.finish()
            }
            .setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}