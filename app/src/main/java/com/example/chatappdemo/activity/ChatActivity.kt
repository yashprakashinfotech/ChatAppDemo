package com.example.chatappdemo.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatappdemo.R
import com.example.chatappdemo.adapter.MessageAdapter
import com.example.chatappdemo.helper.KeyClass
import com.example.chatappdemo.helper.Validation
import com.example.chatappdemo.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageRecyclerView : RecyclerView
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var messageBox : EditText
    private lateinit var sentButton : ImageButton
    private lateinit var messageList : ArrayList<Message>

    private lateinit var mDbRef : DatabaseReference
    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // For Disable Night Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        initView()

        // data receive from UserAdapter
        val name = intent.getStringExtra(KeyClass.KEY_USER_NAME)
        val receiverUid = intent.getStringExtra(KeyClass.KEY_UID)
        supportActionBar?.title = name

        // this is different to all user
        val senderUid = FirebaseAuth.getInstance().currentUser!!.uid

        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid



        // logic for adding data in RecyclerView
        messageRecyclerViewLogic()

        // adding the message to database
        sentButton.setOnClickListener {

            if (!TextUtils.isEmpty(messageBox.text.toString()) && !Validation.isValidSpaceFormat(messageBox.text.toString())){
                val message = messageBox.text.toString()
                val messageObject = Message(message,senderUid)
                mDbRef.child(KeyClass.KEY_CHATS).child(senderRoom!!).child(KeyClass.KEY_MESSAGES).push()
                    .setValue(messageObject).addOnSuccessListener {
                        // for update receiver Room UI
                        mDbRef.child(KeyClass.KEY_CHATS).child(receiverRoom!!).child(KeyClass.KEY_MESSAGES).push()
                            .setValue(messageObject)
                    }
                messageBox.setText("")
            }

        }
    }

    private fun initView(){
        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sentButton = findViewById(R.id.sentButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this,messageList)
        mDbRef = FirebaseDatabase.getInstance().reference
    }
    private fun messageRecyclerViewLogic(){
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter
        mDbRef.child(KeyClass.KEY_CHATS).child(senderRoom!!).child(KeyClass.KEY_MESSAGES)
            .addValueEventListener(object : ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for (postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {}

            })
    }
}