package com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable
data class FrequencyModel(
    val id: Int,
    val label: String,
    var isSelected: Boolean = false,
    var listDateSelected: List<DateCustom>? = null
): Serializable {
    companion object{
        val differUtil = object : DiffUtil.ItemCallback<FrequencyModel>(){
            override fun areItemsTheSame(oldItem: FrequencyModel, newItem: FrequencyModel): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: FrequencyModel, newItem: FrequencyModel): Boolean =
                oldItem.id == newItem.id
        }
    }
}

enum class DateCustom(val abbreviation: String, val label: String) {
    Monday("T2", "Thứ 2"),
    Tuesday("T3", "Thứ 3"),
    Wednesday("T4", "Thứ 4"),
    Thursday("T5", "Thứ 5"),
    Friday("T6", "Thứ 6"),
    Saturday("T7", "Thứ 7"),
    Sunday("CN", "Chủ nhật")
}