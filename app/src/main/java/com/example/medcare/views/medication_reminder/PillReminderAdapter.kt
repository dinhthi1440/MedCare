package com.example.medcare.views.medication_reminder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemReminderBinding
import com.example.medcare.views.medication_reminder.model.PillReminder

class PillReminderAdapter(
    private val onClick: (PillReminder) -> Unit,
    private val onChangeSwitch: (String, Boolean) -> Unit
) : BaseAdapter<PillReminder, BaseViewHolder<PillReminder>>(PillReminder.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PillReminder> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemReminderBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemReminderBinding) :
        BaseViewHolder<PillReminder>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: PillReminder, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtReminderFrequency.text = "\uD83D\uDD04 Tần suất: ${item.frequency}"
                txtReminderLabel.text = item.label
                txtReminderMedicines.text = "\uD83D\uDC8A Thuốc: ${item.medicines}"
                txtReminderTime.text = "⏰ Thời gian: ${item.times}"
                swtOn.isChecked = item.isOn
                root.setOnClickListener {
                    onClick(item)
                }
                swtOn.setOnCheckedChangeListener { _, isOn ->
                    onChangeSwitch(item.id, isOn)
                }
                txtReminderMedicines.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val layout = txtReminderMedicines.layout
                        if (layout != null) {
                            val lines = layout.lineCount
                            if (lines > 0) {
                                val ellipsisCount = layout.getEllipsisCount(lines - 1)
                                if (ellipsisCount > 0) {
                                    txtQuantityMedicine.visibility = View.VISIBLE
                                }
                            }
                        }
                        txtReminderMedicines.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                })
            }
        }
    }
}