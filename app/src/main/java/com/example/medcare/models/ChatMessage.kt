package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class ChatMessage(
    val id: String,
    val senderID: String,
    val content: String,
    val timeMessage: String,
    val timeRead: String,
    val isRead: Boolean,
) : Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem == newItem
        }
    }
}
