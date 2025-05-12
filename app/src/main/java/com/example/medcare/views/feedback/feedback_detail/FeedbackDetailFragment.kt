package com.example.medcare.views.feedback.feedback_detail

import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentFeedbackDetailBinding
import com.example.medcare.models.Feedback
import com.example.medcare.views.feedback.feedback_list.FeedbackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedbackDetailFragment : BaseFragment<FragmentFeedbackDetailBinding>(FragmentFeedbackDetailBinding::inflate) {
    val feedback = Feedback(
        id = "fb001",
        content = "Ứng dụng đôi lúc bị treo khi thêm thuốc.",
        senderID = "acc001",
        senderName = "Nguyễn Văn A",
        senderAvatar = "https://example.com/avatar/a.jpg",
        date = "2025-05-01",
        time = "10:15",
        status = "pending"
    )
    override val viewModel by viewModel<FeedbackViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    override fun bindData() {
        binding.apply {
            txtUserName.text = feedback.senderName
            txtContent.text = "-> ${feedback.content}"
            txtTime.text = "${feedback.date} ${feedback.time}"
            if (feedback.status == "processing") {
                txtStatus.text = "Đang xử lý"
                txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccBlueText))
            } else if(feedback.status == "pending") {
                txtStatus.text = "Chưa xử lý"
                txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccRedText))
            } else {
                txtStatus.text = "Đã xử lý"
                txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccGreen))
            }
        }
    }

    override fun destroy() {

    }
}