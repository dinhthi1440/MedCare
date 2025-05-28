package com.example.medcare.views.feedback.feedback_detail

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentFeedbackDetailBinding
import com.example.medcare.extension.changeStatusFeedback
import com.example.medcare.extension.confirmEvent
import com.example.medcare.views.feedback.feedback_list.FeedbackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedbackDetailFragment : BaseFragment<FragmentFeedbackDetailBinding>(FragmentFeedbackDetailBinding::inflate) {
    private lateinit var feedbackID: String
    private lateinit var previousScreen: String
    private var intiRule = ""
    override val viewModel by viewModel<FeedbackViewModel>()

    override fun initData() {
        feedbackID = arguments?.getString("feedbackID") ?: ""
        previousScreen = arguments?.getString("previousScreen") ?: ""
        viewModel.getFeedbackByID(feedbackID)
    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener { backScreenReset() }
            btnDelete.setOnClickListener {
                dialog(requireContext()).confirmEvent("Xác nhận xóa", "Bạn có chắc chắn muốn xóa phản hồi này?") {
                    viewModel.deleteFeedback(feedbackID)
                }
            }
            btnUpdateStatus.setOnClickListener {
                dialog(requireContext()).changeStatusFeedback(intiRule) {
                    val updates = mapOf(
                        "status" to it,
                    )
                    viewModel.updateFeedback(feedbackID, updates)
                }
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        binding.apply {
            if (previousScreen == "setting_feedback") {
                btnDelete.visibility = View.INVISIBLE
                btnUpdateStatus.visibility = View.INVISIBLE
            }
            viewModel.getFeedbackDetail.observe(viewLifecycleOwner) { feedback ->
                nestedScrollView3.visibility = View.VISIBLE
                txtError.visibility = View.GONE
                txtUserName.text = feedback.senderName
                txtContent.text = "-> ${feedback.content}"
                txtTime.text = "${feedback.date} ${feedback.time}"
                intiRule = feedback.status
                when (feedback.status) {
                    "processing" -> {
                        txtStatus.text = "Đang xử lý"
                        txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccOrangeText))
                    }
                    "pending" -> {
                        txtStatus.text = "Chưa xử lý"
                        txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccRedText))
                    }
                    else -> {
                        txtStatus.text = "Đã xử lý"
                        txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccGreen))
                    }
                }
            }
            viewModel.getUpdateStatus.observe(viewLifecycleOwner) {
                isBackReset = true
                viewModel.getFeedbackByID(feedbackID)
            }
            viewModel.getDeleteStatus.observe(viewLifecycleOwner) {
                if (it == "Success") {
                    isBackReset = true
                    backScreenReset()
                }
            }
            viewModel.messageError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                txtError.text = it
            }
        }
    }

    override fun destroy() {

    }
}