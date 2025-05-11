package com.example.medcare.views.health_advice.chat_doctor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemDoctorBinding
import com.example.medcare.databinding.ItemReminderRelativesBinding
import com.example.medcare.models.Doctor
import com.example.medcare.models.DoctorChat
import com.example.medcare.models.ReminderRelative

class ChatDoctorListAdapter (
    private val onClick: (DoctorChat) -> Unit,
) : BaseAdapter<DoctorChat, BaseViewHolder<DoctorChat>>(DoctorChat.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<DoctorChat> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemDoctorBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemDoctorBinding) :
        BaseViewHolder<DoctorChat>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: DoctorChat, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            val myID = "user_123"
            binding.apply {
                txtDescription.visibility = View.VISIBLE
                txtLastMessageTime.visibility = View.VISIBLE
                if (item.patientID == myID) {
                    txtRelativeName.text = item.patientName
                } else {
                    txtRelativeName.text = item.doctorName
                }
                if (item.isPatientSendLastMessage) {
                    txtDescription.text = "Bạn: ${item.lastMessage}"
                } else {
                    txtDescription.text = item.lastMessage
                    if (item.isReadLastMessage) {
                        txtRelativeName.setTextColor(ContextCompat.getColor(root.context, R.color.ccRedText))
                        txtDescription.setTextColor(ContextCompat.getColor(root.context, R.color.black))
                    }
                }

                txtLastMessageTime.text = "1 giờ"
                root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }
}