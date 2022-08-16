package com.example.kotlinchat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class chatAdapter : RecyclerView.Adapter<chatAdapter.chatHolder>() {
    val VIEW_TYPE_MESSAGE_SENT = 1
    val  VIEW_TYPE_MESSAGE_RECIVED = 2
    class chatHolder (itemview : View): RecyclerView.ViewHolder(itemview) {

    }
    private val diffutil = object :DiffUtil.ItemCallback<chats>(){
        override fun areItemsTheSame(oldItem: chats, newItem: chats): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: chats, newItem: chats): Boolean {
            return oldItem == newItem
        }

    }
    private val recyclerListDiffer = AsyncListDiffer(this,diffutil)


     var chatList :  List<chats>
    get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {

        val chat = chatList.get(position)

        if (chat.user == FirebaseAuth.getInstance().currentUser?.email.toString()){
            return VIEW_TYPE_MESSAGE_SENT
        }else{
            return VIEW_TYPE_MESSAGE_RECIVED
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatHolder {
        if(viewType == VIEW_TYPE_MESSAGE_RECIVED){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row,parent,false)
            return chatHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_right,parent,false)
            return chatHolder(view)
        }

    }

    override fun onBindViewHolder(holder: chatHolder, position: Int) {
        val userText = holder.itemView.findViewById<TextView>(R.id.recycler_row_userText)
        val chatText = holder.itemView.findViewById<TextView>(R.id.recycler_row_ChatText)
        userText.setText(chatList.get(position).user)
        chatText.setText(chatList.get(position).chatText)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}