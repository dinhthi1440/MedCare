package com.example.medcare.views.medication_reminder.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.Medicine
import com.example.medcare.views.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.add_new_reminder.model.SelectedTime
import java.io.Serializable
@Entity(tableName = DataBaseLocal.TABLE_PILL_REMINDER)
data class PillReminder(
    @PrimaryKey
    val id: String,
    val label: String,
    val times: List<SelectedTime>,
    val frequency: FrequencyModel,
    val medicines: List<Medicine>,
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
