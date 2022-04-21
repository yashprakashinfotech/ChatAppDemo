package com.example.chatappdemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappdemo.R
import com.example.chatappdemo.model.Message
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList : ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val itemReceive = 1
    private val itemSent = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == 1){
            // inflate Receive

            val view: View = LayoutInflater.from(context).inflate(R.layout.receive,parent,false)
            ReceiveViewHolder(view)
        } else{
            // inflate Sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent,parent,false)
            SentViewHolder(view)
        }


//        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false)
//        return view
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.javaClass == SentViewHolder::class.java){

            // Do stuff for sent ViewHolder
            val currentMessage = messageList[position]
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else{
            // Do stuff for receive ViewHolder
            val currentMessage = messageList[position]
            val viewHolder = holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if (FirebaseAuth.getInstance().currentUser!!.uid == currentMessage.senderId){
            return itemSent
        }else{
            return itemReceive
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sentMessage : TextView = itemView.findViewById(R.id.txtSentMessage)
    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val receiveMessage : TextView = itemView.findViewById(R.id.txtReceiveMessage)
    }
}