package com.example.medcare.views.connect_relatives.relative_reminder_history

import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentRelativeReminderHistoryBinding
import com.example.medcare.extension.changeStatusHistory
import com.example.medcare.models.ReminderHistory
import com.example.medcare.views.reminder_history.reminder_history_list.ReminderHistoryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class RelativeReminderHistoryFragment : BaseFragment<FragmentRelativeReminderHistoryBinding>(FragmentRelativeReminderHistoryBinding::inflate) {
    private val reminderHistoryAdapter by lazy {
        ReminderHistoryAdapter(
            ::onclickHistoryItem,
            ::onChangeStatus,
        )
    }
    override val viewModel by viewModel<RelativeHistoryViewModel>()

    override fun initData() {

    }

    override fun handleEvent() {

    }

    override fun bindData() {
        viewModel.getHistoryList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.txtEmptyList.visibility = View.VISIBLE
                binding.rcvReminderHistoryList.visibility = View.GONE
            } else {
                binding.txtEmptyList.visibility = View.GONE
                binding.rcvReminderHistoryList.visibility = View.VISIBLE
                binding.rcvReminderHistoryList.layoutManager = LinearLayoutManager(binding.root.context)
                reminderHistoryAdapter.submitList(it)
                binding.rcvReminderHistoryList.adapter = reminderHistoryAdapter
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