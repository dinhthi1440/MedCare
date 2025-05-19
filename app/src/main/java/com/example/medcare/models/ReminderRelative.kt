package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class ReminderRelative(
    var id: String = "",
    var senderID: String = "",
    var senderName: String = "",
    var senderAvatar: String = "",
    var senderDescription: String? = "",
    var receiverID: String = "",
    var receiverName: String = "",
    var receiverAvatar: String = "",
    var receiverDescription: String? = "",
    var pillReminder: PillReminder = PillReminder()
): Serializable {
    constructor(
        senderID: String,
        receiverID: String,
        pillReminder: PillReminder
    ) : this(
        id = "",
        senderID = senderID,
        senderName = "",
        senderAvatar = "",
        senderDescription = null,
        receiverID = receiverID,
        receiverName = "",
        receiverAvatar = "",
        receiverDescription = null,
        pillReminder = pillReminder
    )
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<ReminderRelative>() {
            override fun areItemsTheSame(oldItem: ReminderRelative, newItem: ReminderRelative): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ReminderRelative, newItem: ReminderRelative): Boolean =
                oldItem == newItem
        }
    }
}
