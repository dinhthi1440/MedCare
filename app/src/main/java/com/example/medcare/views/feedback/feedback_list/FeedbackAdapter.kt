package com.example.medcare.views.feedback.feedback_list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.medcare.R
import com.example.medcare.base.BaseAdapter
import com.example.medcare.base.BaseViewHolder
import com.example.medcare.databinding.ItemFeedbackBinding
import com.example.medcare.models.Feedback

class FeedbackAdapter (
    private val onClick: (Feedback) -> Unit,
) : BaseAdapter<Feedback, BaseViewHolder<Feedback>>(Feedback.differUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<Feedback> {
        val inflate = LayoutInflater.from(parent.context)
        val binding = ItemFeedbackBinding.inflate(inflate, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemFeedbackBinding) :
        BaseViewHolder<Feedback>(binding) {
        @SuppressLint("SetTextI18n")
        override fun bindView(item: Feedback, isItemSelected: Boolean) {
            super.bindView(item, isItemSelected)
            binding.apply {
                txtFullName.text = item.senderName
                txtFeedbackContent.text = "-> ${item.content}"
                txtTime.text = "${item.date} ${item.time}"
                if (item.status == "processing") {
                    txtHandleStatus.text = "Đang xử lý"
                    txtHandleStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccBlueText))
                } else if(item.status == "pending") {
                    txtHandleStatus.text = "Chưa xử lý"
                    txtHandleStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccRedText))
                } else {
                    txtHandleStatus.text = "Đã xử lý"
                    txtHandleStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccGreen))
                }
                root.setOnClickListener {
                    onClick(item)
                }
            }

        }
    }
}