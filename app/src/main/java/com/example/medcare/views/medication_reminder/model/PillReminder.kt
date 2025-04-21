package com.example.medcare.views.medication_reminder.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medcare.data.database.local.DataBaseLocal
import java.io.Serializable
@Entity(tableName = DataBaseLocal.TABLE_PILL_REMINDER)
data class PillReminder(
    @PrimaryKey
    val id: String,
    val label: String,
    val times: String,
    val frequency: String,
    val medicines: String,
    val isOn: Boolean,
    val note: String,
    val disease: String
): Serializable {
    companion object{
        val differUtil = object : DiffUtil.ItemCallback<PillReminder>(){
            override fun areItemsTheSame(oldItem: PillReminder, newItem: PillReminder): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PillReminder, newItem: PillReminder): Boolean =
                oldItem.id == newItem.id
        }
    }
}
