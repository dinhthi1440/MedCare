package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class DoctorChat(
    val id: String,
    val patientID: String,
    val patientName: String,
    val patientAvatar: String,
    val doctorID: String,
    val doctorName: String,
    val doctorAvatar: String,
    val lastMessage: String,
    val timeLastMessage: String,
    val isReadLastMessage: Boolean,
    val timeReadLastMessage: String,
    val isPatientSendLastMessage: Boolean
) : Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<DoctorChat>() {
            override fun areItemsTheSame(oldItem: DoctorChat, newItem: DoctorChat): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: DoctorChat, newItem: DoctorChat): Boolean =
                oldItem == newItem
        }
    }
}