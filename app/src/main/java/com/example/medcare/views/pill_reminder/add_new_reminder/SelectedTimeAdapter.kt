package com.example.medcare.views.pill_reminder.add_new_reminder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemSelectedTimeBinding
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime

class SelectedTimeAdapter(
    private val onDelete: (Int) -> Unit,
) : BaseAdapter<SelectedTime, BaseViewHolder<SelectedTime>>(SelectedTime.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SelectedTime> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemSelectedTimeBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemSelectedTimeBinding) :
        BaseViewHolder<SelectedTime>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: SelectedTime, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtSelectedTime.text = item.time
                btnRemove.setOnClickListener {
                    onDelete(item.id)
                }
            }
        }
    }
}