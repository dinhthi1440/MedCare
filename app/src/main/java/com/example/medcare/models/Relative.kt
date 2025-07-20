package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Relative(
    var id: String = "",
    var fullName: String = "",
    var relativeTitle: String = "",
    var avatar: String = "",

    var canInsertMedicine: Boolean = true,
    var canInsertReminder: Boolean = true,
    var canSeeReminderHistory: Boolean = true,

    var myCanInsertMedicine: Boolean = false,
    var myCanInsertReminder: Boolean = false,
    var myCanSeeReminderHistory: Boolean = false
)
: Serializable {
    constructor() : this("", "", "", "", true, true, true, true, true, true)
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Relative>() {
            override fun areItemsTheSame(oldItem: Relative, newItem: Relative): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Relative, newItem: Relative): Boolean =
                oldItem == newItem
        }
    }
}
