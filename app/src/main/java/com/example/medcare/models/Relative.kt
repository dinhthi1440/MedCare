package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Relative(
    val id: String,
    val name: String,
    val relativeTitle: String,
    val avatar: String
): Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Relative>() {
            override fun areItemsTheSame(oldItem: Relative, newItem: Relative): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Relative, newItem: Relative): Boolean =
                oldItem == newItem
        }
    }
}
