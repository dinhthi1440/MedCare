package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class ChatMessage(
    var id: String,
    var senderID: String,
    var content: String,
    var timeMessage: String,
    var timeMessageLong: Long,
    var timeRead: String,
    var isRead: Boolean,
) : Serializable {
    constructor() : this("" , "", "", "", 0, "",false)
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem == newItem
        }
    }
}
