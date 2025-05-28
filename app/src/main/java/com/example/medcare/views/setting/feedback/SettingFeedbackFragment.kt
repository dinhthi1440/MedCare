package com.example.medcare.views.setting.feedback

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentSettingFeebackBinding
import com.example.medcare.extension.confirmFeedbackInput
import com.example.medcare.extension.getData
import com.example.medcare.extension.showImage
import com.example.medcare.models.Account
import com.example.medcare.models.Feedback
import com.example.medcare.utils.Constants
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class SettingFeedbackFragment :
    BaseFragment<FragmentSettingFeebackBinding>(FragmentSettingFeebackBinding::inflate) {
    private val feedbackAdapter by lazy { FeedbackAdapter(::onclickFeedbackItem) }
    private var account: Account? = null

    override val viewModel by viewModel<SettingFeedbackViewModel>()

    override fun initData() {
        val json = sharedPreferences.getData(Constants.SHARED_USER)
        account = gson.fromJson(json, Account::class.java)
        viewModel.getFeedbackList(uid)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnAdd.setOnClickListener {
            dialog(requireContext()).confirmFeedbackInput {
                val current = LocalDateTime.now()
                val time = current.format(DateTimeFormatter.ofPattern("HH:mm"))
                val date = current.format(DateTimeFormatter.ofPattern("dd/MM/yy"))
                val feedback = Feedback (
                    UUID.randomUUID().toString(), it, uid,
                    account?.fullName ?: "",
                    account?.avatar ?: "",
                    date, time, "pending"
                )
                viewModel.insertFeedback(feedback)
            }
        }
    }

    override fun bindData() {
        viewModel.getFeedbacks.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.rcvFeedbackList.visibility = View.VISIBLE
                binding.txtEmptyList.visibility = View.GONE
                binding.rcvFeedbackList.layoutManager = LinearLayoutManager(binding.root.context)
                feedbackAdapter.submitList(it)
                binding.rcvFeedbackList.adapter = feedbackAdapter
            } else {
                binding.rcvFeedbackList.visibility = View.GONE
                binding.txtEmptyList.visibility = View.VISIBLE
            }
        }
        viewModel.getInsertStatus.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.getFeedbackList(uid)
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            binding.txtEmptyList.text = it
        }
    }

    private fun onclickFeedbackItem(feedback: Feedback) {
        val bundle = Bundle().apply {
            putString("feedbackID", feedback.id)
            putString("previousScreen", "setting_feedback")
        }
        findNavController().navigate(R.id.action_settingFeedbackFragment_to_feedbackDetailFragment, bundle)
    }

    override fun destroy() {

    }

}