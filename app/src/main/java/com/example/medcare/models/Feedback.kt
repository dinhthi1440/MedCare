package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Feedback(
    var id: String,
    var content: String,
    var senderID: String,
    var senderName: String,
    var senderAvatar: String,
    var date: String,
    var time: String,
    var status: String
) : Serializable {
    constructor() : this("", "", "", "", "", "", "", "")
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Feedback>() {
            override fun areItemsTheSame(oldItem: Feedback, newItem: Feedback): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Feedback, newItem: Feedback): Boolean =
                oldItem == newItem
        }
    }
}
