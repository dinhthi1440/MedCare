package com.example.medcare.views.health_advice.chat_doctor_detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemChatMessageBinding
import com.example.medcare.databinding.ItemDoctorBinding
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.Doctor

class ChatListAdapter(private val currentUserID: String) :
    BaseAdapter<ChatMessage, BaseViewHolder<ChatMessage>>(ChatMessage.differUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ChatMessage> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemChatMessageBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemChatMessageBinding) :
        BaseViewHolder<ChatMessage>(binding) {

        override fun bindView(item: ChatMessage, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                if (item.senderID == currentUserID) {
                    // Mình gửi
                    rightMessageContainer.visibility = View.VISIBLE
                    txtRightMessage.text = item.content

                    leftMessageContainer.visibility = View.GONE
                } else {
                    // Người khác gửi
                    leftMessageContainer.visibility = View.VISIBLE
                    txtLeftMessage.text = item.content

                    rightMessageContainer.visibility = View.GONE
                }
            }
        }
    }
}
