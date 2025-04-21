package com.example.medcare.views.add_new_reminder.add_frequency

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemFrequencySelectBinding

class FrequencyAdapter(
    private val items: List<FrequencyModel>,
    private val selectFrequency: (FrequencyModel) -> Unit,
    private val selectCustom: () -> Unit
): BaseAdapter<FrequencyModel, BaseViewHolder<FrequencyModel>>(FrequencyModel.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<FrequencyModel> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemFrequencySelectBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemFrequencySelectBinding) :
        BaseViewHolder<FrequencyModel>(binding) {
        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
        override fun bindView(item: FrequencyModel, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtLabel.text = item.label
                container.isSelected = item.isSelected
                if (item.label == "Tuỳ chỉnh") imgIcArrow.visibility = View.VISIBLE
                if (item.isSelected) {
                    imgIcIsSelected.visibility = View.VISIBLE
                    txtLabel.setTextColor(ContextCompat.getColor(root.context, R.color.ccBlueTextSelected))
                } else {
                    imgIcIsSelected.visibility = View.INVISIBLE
                    txtLabel.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                }
                root.setOnClickListener {
                    items.forEachIndexed { index, frequencyModel -> frequencyModel.isSelected = (frequencyModel.id == item.id)  }
                    selectFrequency(item)
                    notifyDataSetChanged()
                    if (item.label == "Tuỳ chỉnh"){
                        selectCustom()
                    }
                }
            }
        }
    }
}