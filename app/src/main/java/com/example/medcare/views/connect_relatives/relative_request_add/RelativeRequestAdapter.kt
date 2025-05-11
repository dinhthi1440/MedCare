package com.example.medcare.views.connect_relatives.relative_request_add

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemRelativeBinding
import com.example.medcare.databinding.ItemReminderRelativesBinding
import com.example.medcare.models.Relative
import com.example.medcare.models.ReminderRelative

class RelativeRequestAdapter (
    private val onClick: (ReminderRelative) -> Unit,
) : BaseAdapter<ReminderRelative, BaseViewHolder<ReminderRelative>>(ReminderRelative.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ReminderRelative> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemReminderRelativesBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemReminderRelativesBinding) :
        BaseViewHolder<ReminderRelative>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: ReminderRelative, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            val myID = "user_123"
            binding.apply {
                if(item.senderID == myID) {
                    cvAvtTo.visibility = View.VISIBLE
                    txtFromOrTo.text = "Tới: ${item.receiverName}"
                } else {
                    cvAvtFrom.visibility = View.VISIBLE
                    txtFromOrTo.text = "Từ: ${item.senderName}"
                }
                txtReminderLabel.text = "\uD83D\uDCCC ${item.pillReminder.label}"
                val timeList = item.pillReminder.times
                txtReminderTime.text = "⏰ Thời gian: ${timeList.map { it.time }.joinToString(", ")}"
                val medicineList = item.pillReminder.medicines
                txtReminderMedicines.text = "⏰ Thời gian: ${medicineList.map { it.name }.joinToString(", ")}"
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
                root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }
}