package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Doctor(
    var id: String = "",
    var fullName: String = "",
    var avatar: String = ""
) : Serializable {
    constructor() : this ("", "", "")
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Doctor>() {
            override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean =
                oldItem == newItem
        }
    }
}
