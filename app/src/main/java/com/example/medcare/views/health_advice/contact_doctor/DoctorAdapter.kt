package com.example.medcare.views.health_advice.contact_doctor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemDoctorBinding
import com.example.medcare.databinding.ItemReminderRelativesBinding
import com.example.medcare.models.Doctor
import com.example.medcare.models.ReminderRelative

class DoctorAdapter (
    private val onClick: (Doctor) -> Unit,
) : BaseAdapter<Doctor, BaseViewHolder<Doctor>>(Doctor.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Doctor> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemDoctorBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemDoctorBinding) :
        BaseViewHolder<Doctor>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: Doctor, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtRelativeName.text = item.name
                root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }
}