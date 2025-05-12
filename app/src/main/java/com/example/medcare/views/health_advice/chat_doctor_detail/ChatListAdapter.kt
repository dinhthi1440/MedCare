package com.example.medcare.views.health_advice.chat_doctor_detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemChatMessageBinding
import com.example.medcare.databinding.ItemDoctorBinding
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.Doctor

class ChatListAdapter(private val currentUserID: String)  : RecyclerView.Adapter<MessageHolder>() {

    private var listOfMessage = listOf<ChatMessage>()

    private val LEFT = 0
    private val RIGHT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == RIGHT) {
            val view = inflater.inflate(R.layout.item_chat_right, parent, false)
            MessageHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_chat_left, parent, false)
            MessageHolder(view)
        }
    }
    override fun getItemCount() = listOfMessage.size
    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val message = listOfMessage[position]
        holder.messageText.visibility = View.VISIBLE
        holder.timeOfSent.visibility = View.VISIBLE
        holder.messageText.text = message.content
        holder.timeOfSent.text = "11:30"
    }

    override fun getItemViewType(position: Int) =
        if (listOfMessage[position].senderID == currentUserID) RIGHT else LEFT

    fun setList(newList: List<ChatMessage>) {

        this.listOfMessage = newList

    }

}

class MessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView.rootView) {
    val messageText: TextView = itemView.findViewById(R.id.txtv_show_mess)
    val timeOfSent: TextView = itemView.findViewById(R.id.txtv_time_send)
}