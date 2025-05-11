package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class ReminderRelative(
    val id: String,
    val senderID: String,
    val senderName: String,
    val senderAvatar: String,
    val senderDescription: String? = "",
    val receiverID: String,
    val receiverName: String,
    val receiverAvatar: String,
    val receiverDescription: String? = "",
    val pillReminder: PillReminder
): Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<ReminderRelative>() {
            override fun areItemsTheSame(oldItem: ReminderRelative, newItem: ReminderRelative): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ReminderRelative, newItem: ReminderRelative): Boolean =
                oldItem == newItem
        }
    }
}
