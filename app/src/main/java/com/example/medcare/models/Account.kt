package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Account(
    val id: String,
    val fullName: String,
    val userName: String,
    val email: String,
    val avatar: String,
    val password: String,
    val rule: String,
    val status: String
) : Serializable {
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem == newItem
        }
    }
}
