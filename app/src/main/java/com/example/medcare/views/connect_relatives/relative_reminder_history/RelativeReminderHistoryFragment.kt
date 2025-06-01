package com.example.medcare.views.connect_relatives.relative_reminder_history

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentRelativeReminderHistoryBinding
import com.example.medcare.extension.changeStatusHistory
import com.example.medcare.extension.editHistoryRelative
import com.example.medcare.models.Relative
import com.example.medcare.models.ReminderHistory
import com.example.medcare.views.reminder_history.reminder_history_list.ReminderHistoryAdapter
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class RelativeReminderHistoryFragment : BaseFragment<FragmentRelativeReminderHistoryBinding>(FragmentRelativeReminderHistoryBinding::inflate) {
    private lateinit var relative: Relative
    private val reminderHistoryAdapter by lazy {
        ReminderHistoryAdapter(
            ::onclickHistoryItem,
            ::onChangeStatus,
        )
    }
    override val viewModel by viewModel<RelativeHistoryViewModel>()

    override fun initData() {
        val json = arguments?.getString("relative")
        if (json != null) {
            relative = Gson().fromJson(json, Relative::class.java)
        }
        viewModel.getRelativeHistoryByRelativeID(uid, relative.id)
    }

    @SuppressLint("SetTextI18n")
    override fun handleEvent() {
        binding.cvEdit.setOnClickListener {
            dialog(requireContext()).editHistoryRelative(relative) { rawRelative ->
                viewModel.updateRelativeHistory(uid, rawRelative)
            }
        }
        binding.btnBack.setOnClickListener {
            backScreenReset()
        }
    }

    override fun bindData() {
        binding.apply {
            if (relative.avatar.isNotBlank()) {
                Glide.with(requireContext()).load(relative.avatar).into(imgAvatar)
            }
            txtRelativeName.text = "${relative.fullName} (${relative.relativeTitle})"

            viewModel.getHistoryList.observe(viewLifecycleOwner) {
                txtEmptyList.visibility = View.GONE
                rcvReminderHistoryList.visibility = View.VISIBLE
                rcvReminderHistoryList.layoutManager = LinearLayoutManager(root.context)
                reminderHistoryAdapter.submitList(it)
                rcvReminderHistoryList.adapter = reminderHistoryAdapter
            }
            viewModel.messageError.observe(viewLifecycleOwner) {
                txtEmptyList.text = it
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
            viewModel.getUpdateStatus.observe(viewLifecycleOwner) { updatedRelative  ->
                isBackReset = true
                relative = updatedRelative
                binding.txtRelativeName.text = "${updatedRelative.fullName} (${updatedRelative.relativeTitle})"
            }
        }
    }
    private fun onclickHistoryItem(reminderHistory: ReminderHistory) {
        findNavController().navigate(R.id.action_reminderHistoryFragment_to_reminderHistoryDetailFragment)
    }

    private fun onChangeStatus(reminderHistory: ReminderHistory) {
        dialog(requireContext()).changeStatusHistory() {
            when (it) {
                "Drank" -> {

                }
                "Missed" -> {

                }
                else -> {

                }
            }
        }
    }
    override fun destroy() {

    }
}