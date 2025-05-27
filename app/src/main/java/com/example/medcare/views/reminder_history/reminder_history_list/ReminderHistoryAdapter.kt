package com.example.medcare.views.reminder_history.reminder_history_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemReminderHistoryBinding
import com.example.medcare.models.HistoryStatus
import com.example.medcare.models.ReminderHistory

class ReminderHistoryAdapter(private val onClick: (ReminderHistory) -> Unit,
                             private val setStatus: (ReminderHistory) -> Unit,
    ) : BaseAdapter<ReminderHistory, BaseViewHolder<ReminderHistory>>(ReminderHistory.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ReminderHistory> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemReminderHistoryBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemReminderHistoryBinding) :
        BaseViewHolder<ReminderHistory>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: ReminderHistory, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtReminderLabel.text = "\uD83D\uDCCC ${item.label}"
                txtReminderDay.text = "Ngày: ${item.date}"
                txtReminderTime.text = item.time
                txtReminderHisStatus.text = item.status
                if (item.status == HistoryStatus.DRANK.status) {
                    imgCheckSuccess.visibility = View.VISIBLE
                    imgMissing.visibility = View.GONE
                    txtStatusBtn.visibility = View.GONE
                    txtReminderHisStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccGreen))
                    layoutReminderHistory.setBackgroundResource(R.drawable.bg_reminder_history_green)
                } else if (item.status == HistoryStatus.MISSED.status) {
                    imgCheckSuccess.visibility = View.GONE
                    imgMissing.visibility = View.VISIBLE
                    txtStatusBtn.visibility = View.GONE
                    txtReminderHisStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccRed))
                    layoutReminderHistory.setBackgroundResource(R.drawable.bg_reminder_history_red)
                } else {
                    imgCheckSuccess.visibility = View.GONE
                    imgMissing.visibility = View.GONE
                    txtStatusBtn.visibility = View.VISIBLE
                    txtReminderHisStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccBlueText))
                    layoutReminderHistory.setBackgroundResource(R.drawable.bg_reminder_history)
                }

                txtStatusBtn.setOnClickListener {
                    setStatus(item)
                }
                root.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }
}