package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Doctor(
    val id: String,
    val name: String,
    val avatar: String
) : Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Doctor>() {
            override fun areItemsTheSame(oldItem: Doctor, newItem: Doctor): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Doctor, newItem: Doctor): Boolean =
                oldItem == newItem
        }
    }
}
