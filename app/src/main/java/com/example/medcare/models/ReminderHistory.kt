package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.PrimaryKey
import java.io.Serializable

data class ReminderHistory(
    @PrimaryKey
    val id: String,
    val label: String,
    val date: String,
    val time: String,
    var status: String,
    val reminder: PillReminder
) : Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<ReminderHistory>() {
            override fun areItemsTheSame(oldItem: ReminderHistory, newItem: ReminderHistory): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ReminderHistory, newItem: ReminderHistory): Boolean =
                oldItem == newItem
        }
    }
}