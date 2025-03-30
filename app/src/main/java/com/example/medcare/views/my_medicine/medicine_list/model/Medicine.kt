package com.example.medcare.views.my_medicine.medicine_list.model

import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

class Medicine(
    val id: String,
    val name: String,
    val image: String,
    val expirationDate: String,
    val quantity: Int,
    val unit: String
): Serializable {
    companion object{
        val differUtil = object : DiffUtil.ItemCallback<Medicine>(){
            override fun areItemsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Medicine, newItem: Medicine): Boolean =
                oldItem.id == newItem.id
        }
    }
    constructor() : this("", "", "", "",  0, "")
}