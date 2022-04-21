package com.example.chatappdemo.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappdemo.R
import com.example.chatappdemo.activity.ChatActivity
import com.example.chatappdemo.helper.KeyClass
import com.example.chatappdemo.model.User
import com.google.firebase.auth.FirebaseAuth

class UserAdapter(val context: Context, val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val user = userList[position]
        holder.txtName.text = user.name
        holder.itemView.setOnClickListener {
            val i = Intent(context,ChatActivity::class.java)
            i.putExtra(KeyClass.KEY_USER_NAME,user.name)
            i.putExtra(KeyClass.KEY_UID,user.uId)
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val txtName : TextView = itemView.findViewById(R.id.txtName)

    }


}