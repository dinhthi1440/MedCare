package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Feedback(
    val id: String,
    val content: String,
    val senderID: String,
    val senderName: String,
    val senderAvatar: String,
    val date: String,
    val time: String,
    val status: String
) : Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Feedback>() {
            override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback): Boolean =
                oldItem == newItem
        }
    }
}
