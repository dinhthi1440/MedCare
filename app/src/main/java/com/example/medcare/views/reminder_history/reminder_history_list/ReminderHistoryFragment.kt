package com.example.medcare.views.reminder_history.reminder_history_list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentReminderHistoryBinding
import com.example.medcare.extension.changeStatusHistory
import com.example.medcare.models.HistoryStatus
import com.example.medcare.models.ReminderHistory


class ReminderHistoryFragment : BaseFragment<FragmentReminderHistoryBinding>(FragmentReminderHistoryBinding::inflate) {
    override val viewModel by viewModel<ReminderHistoryViewModel>()
    private val reminderHistoryAdapter by lazy {
        ReminderHistoryAdapter(
            ::onclickHistoryItem,
            ::onChangeStatus,
        )
    }
    override fun initData() {
        viewModel.getHistoryList(uid)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

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
        viewModel.getHistoryUpdateStatus.observe(viewLifecycleOwner) {
            viewModel.getHistoryList(uid)
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            binding.txtEmptyList.text = it
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun onclickHistoryItem(reminderHistory: ReminderHistory) {
        val bundle = Bundle().apply {
            putString("history_id", reminderHistory.id)
        }
        findNavController().navigate(R.id.action_reminderHistoryFragment_to_reminderHistoryDetailFragment, bundle)

    }

    private fun onChangeStatus(reminderHistory: ReminderHistory) {
        dialog(requireContext()).changeStatusHistory() {
            when (it) {
                HistoryStatus.DRANK.status -> {
                    viewModel.updateHistory(uid, reminderHistory, HistoryStatus.DRANK.status )
                }
                HistoryStatus.MISSED.status-> {
                    viewModel.updateHistory(uid, reminderHistory, HistoryStatus.DRANK.status )
                }
            }
        }
    }


    override fun destroy() {

    }
}