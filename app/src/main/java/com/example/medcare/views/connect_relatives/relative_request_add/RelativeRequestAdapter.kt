package com.example.medcare.views.connect_relatives.relative_request_add

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.bumptech.glide.Glide
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemReminderRelativesBinding
import com.example.medcare.models.PillReminder
import com.example.medcare.models.PillReminderResult
import com.example.medcare.models.ReminderRequestStatus

class RelativeRequestAdapter (
    private val myID: String,
    private val onClick: (PillReminder) -> Unit,
) : BaseAdapter<PillReminder, BaseViewHolder<PillReminder>>(PillReminder.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<PillReminder> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemReminderRelativesBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemReminderRelativesBinding) :
        BaseViewHolder<PillReminder>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: PillReminder, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                if(item.senderID == myID) {
                    cvAvtTo.visibility = View.VISIBLE
                    if (item.statusRequest == ReminderRequestStatus.REQUESTING.status) {
                        imgStatusActiveNoTo.visibility = View.VISIBLE
                    } else {
                        imgStatusActiveYesTo.visibility = View.VISIBLE
                    }
                    txtFromOrTo.text = "Tới: ${item.receiverName}"
                    if(item.receiverAvatar != "") {
                        Glide.with(root.context).load(item.receiverAvatar).into(imgUserFrom)
                    }

                } else {
                    cvAvtFrom.visibility = View.VISIBLE
                    if (item.statusRequest == ReminderRequestStatus.REQUESTING.status) {
                        imgStatusActiveNoFrom.visibility = View.VISIBLE
                    } else {
                        imgStatusActiveYesFrom.visibility = View.VISIBLE
                    }
                    txtFromOrTo.text = "Từ: ${item.senderName}"
                    if(item.senderAvatar != "") {
                        Glide.with(root.context).load(item.senderAvatar).into(imgUserFrom)
                    }
                }
                root.setOnClickListener {
                    onClick(item)
                }
                txtReminderLabel.text = "\uD83D\uDCCC ${item.label}"
                val timeList = item.times
                txtReminderTime.text = "⏰ Thời gian: ${timeList.map { it.time }.joinToString(", ")}"
                val medicineList = item.medicines
                txtReminderMedicines.text = "\uD83D\uDC8A Thuốc: ${medicineList.map { it.name }.joinToString(", ")}"
                txtReminderMedicines.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        val layout = txtReminderMedicines.layout
                        if (layout != null) {
                            val lines = layout.lineCount
                            if (lines > 0) {
                                val ellipsisCount = layout.getEllipsisCount(lines - 1)
                                if (ellipsisCount > 0) {
                                    txtQuantityMedicine.visibility = View.VISIBLE
                                    txtQuantityMedicine.text = "(${medicineList.size} loại thuốc)"
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