package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class DoctorChat(
    var id: String = "",
    var patientID: String = "",
    var patientName: String = "",
    var patientAvatar: String = "",
    var doctorID: String = "",
    var doctorName: String = "",
    var doctorAvatar: String = "",
    var lastMessage: String = "",
    var timeLastMessage: String = "",
    var isReadLastMessage: Boolean = false,
    var timeReadLastMessage: String = "",
    var lastMessageSenderID: String = "",
) : Serializable {
    constructor() : this(
        "", "", "", "", "", "", "",
        "", "", false, "", ""
    )

    companion object {
        val differUtil = object : DiffUtil.ItemCallback<DoctorChat>() {
            override fun areItemsTheSame(oldItem: DoctorChat, newItem: DoctorChat): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DoctorChat, newItem: DoctorChat): Boolean =
                oldItem == newItem
        }
    }
}