package com.example.medcare.views.pill_reminder

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemReminderBinding
import com.example.medcare.models.PillReminder

class PillReminderAdapter(
    private val onClick: (PillReminder) -> Unit,
    private val onChangeSwitch: (PillReminder, Boolean) -> Unit,
    private val onRemove: (PillReminder) -> Unit
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
            Log.e("TAG", "bindView: item là ${item}", )
            binding.apply {
                txtReminderFrequency.text = "\uD83D\uDD04 Tần suất: ${item.frequency.label}"
                txtReminderLabel.text = item.label
                txtReminderTime.text = "⏰ Thời gian: ${item.times.first().time}"
                swtOn.isChecked = item.isOn
                root.setOnClickListener {
                    layoutDeleteBtn.visibility = View.GONE
                    onClick(item)
                }
                root.setOnLongClickListener {
                    layoutDeleteBtn.visibility = View.VISIBLE
                    layoutDeleteBtn.alpha = 0f
                    layoutDeleteBtn.translationX = 200f
                    layoutDeleteBtn.animate()
                        .alpha(1f)
                        .translationX(0f)
                        .setDuration(300)
                        .start()
                    true
                }
                swtOn.setOnCheckedChangeListener { _, isOn ->
                    onChangeSwitch(item, isOn)
                }
                layoutDeleteBtn.setOnClickListener {
                    onRemove(item)
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