package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import java.io.Serializable

@Entity(tableName = DataBaseLocal.TABLE_PILL_REMINDER)
data class PillReminder(
    @PrimaryKey
    var id: String = "",
    var label: String = "",
    var times: List<SelectedTime> = emptyList(),
    var frequency: FrequencyModel = FrequencyModel(),
    var medicines: List<Medicine> = emptyList(),
    var isOn: Boolean = false,
    var note: String = "",
    var disease: String = "",
    var senderID: String = "",
    var senderName: String = "",
    var senderAvatar: String = "",
    var senderDescription: String? = "",
    var receiverID: String = "",
    var receiverName: String = "",
    var receiverAvatar: String = "",
    var receiverDescription: String? = "",
    var statusRequest: String? = "",
    var createAt: String = "",
    var updateAt: String = "",
) : Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<PillReminder>() {
            override fun areItemsTheSame(oldItem: PillReminder, newItem: PillReminder): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: PillReminder, newItem: PillReminder): Boolean =
                oldItem == newItem
        }
    }

    constructor() : this(
        "",
        "",
        emptyList(),
        FrequencyModel(),
        emptyList(),
        false,
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        ""
    )
}

enum class ReminderRequestStatus(val status: String) {
    REQUESTING("requesting"),
    ACCEPTED("accepted"),
    DELETED("deleted"),
    CANCEL("canceled")
}
