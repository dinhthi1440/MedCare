package com.example.medcare.views.feedback.feedback_detail

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentFeedbackDetailBinding
import com.example.medcare.views.feedback.feedback_list.FeedbackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedbackDetailFragment : BaseFragment<FragmentFeedbackDetailBinding>(FragmentFeedbackDetailBinding::inflate) {
    private lateinit var feedbackID: String
    override val viewModel by viewModel<FeedbackViewModel>()

    override fun initData() {
        feedbackID = arguments?.getString("feedbackID") ?: ""
        viewModel.getFeedbackByID(feedbackID)
    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        binding.apply {
            viewModel.getFeedbackDetail.observe(viewLifecycleOwner) { feedback ->
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
            viewModel.getUpdateStatus.observe(viewLifecycleOwner) {
                isBackReset = true
                viewModel.getFeedbackByID(feedbackID)
            }
            viewModel.getDeleteStatus.observe(viewLifecycleOwner) {
                if (it == "Success") {
                    Toast.makeText(context, "Đã xóa người dùng thành công", Toast.LENGTH_SHORT)
                        .show()
                    isBackReset = true
                    backScreenReset()
                }
            }
            viewModel.messageError.observe(viewLifecycleOwner) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                //btnDelete.visibility = View.INVISIBLE
                //txtf.text = it
            }
        }
    }

    override fun destroy() {

    }
}