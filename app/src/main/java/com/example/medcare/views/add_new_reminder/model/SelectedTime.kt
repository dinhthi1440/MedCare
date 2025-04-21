package com.example.medcare.views.add_new_reminder.model

import androidx.recyclerview.widget.DiffUtil
import com.example.medcare.views.medication_reminder.model.PillReminder
import java.io.Serializable

data class SelectedTime(
    val id: Int,
    val time: String,
    val amPm: String
): Serializable {
    companion object{
        val differUtil = object : DiffUtil.ItemCallback<SelectedTime>(){
            override fun areItemsTheSame(oldItem: SelectedTime, newItem: SelectedTime): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SelectedTime, newItem: SelectedTime): Boolean =
                oldItem.id == newItem.id
        }
    }
}