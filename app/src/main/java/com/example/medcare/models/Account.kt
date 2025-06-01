package com.example.medcare.models

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

data class Account(
    var id: String,
    var fullName: String,
    var email: String,
    var avatar: String,
    var rule: String,
    var status: String,
    var createAt: String,
    var updateAt: String,
) : Serializable {
    constructor() : this("", "" ,"" ,"", "", "", "", "")
    companion object {
        val differUtil = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem == newItem
        }
    }
}
