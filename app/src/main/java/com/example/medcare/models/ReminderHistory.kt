package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medcare.data.database.local.DataBaseLocal
import java.io.Serializable

@Entity(tableName = DataBaseLocal.TABLE_HISTORY_REMINDER)
data class ReminderHistory(
    @PrimaryKey
    var id: String = "",
    var label: String = "",
    var date: String = "",
    var time: String = "",
    var status: String = "",
    var reminder: PillReminder = PillReminder()
) : Serializable {
    constructor() : this( "", "", "", "", "", PillReminder())
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<ReminderHistory>() {
            override fun areItemsTheSame(oldItem: ReminderHistory, newItem: ReminderHistory): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ReminderHistory, newItem: ReminderHistory): Boolean =
                oldItem == newItem
        }
    }
}

enum class HistoryStatus(val status: String) {
    DRANK("Đã uống"),
    MISSED("Bỏ lỡ"),
    NOT_CONFIRMED("Xác nhận")
}

