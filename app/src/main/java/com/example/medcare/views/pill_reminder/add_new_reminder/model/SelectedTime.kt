package com.example.medcare.views.pill_reminder.add_new_reminder.model

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class SelectedTime(
    val id: Int,
    val time: String,
    val amPm: String
): Serializable {
    fun to24HourFormat(): String {
        val parts = time.split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()

        val hour24 = when (amPm.uppercase()) {
            "AM" -> if (hour == 12) 0 else hour
            "PM" -> if (hour < 12) hour + 12 else hour
            else -> hour
        }

        return String.format("%02d:%02d", hour24, minute)
    }
    companion object{
        val differUtil = object : DiffUtil.ItemCallback<SelectedTime>(){
            override fun areItemsTheSame(oldItem: SelectedTime, newItem: SelectedTime): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SelectedTime, newItem: SelectedTime): Boolean =
                oldItem.id == newItem.id
        }
    }
}