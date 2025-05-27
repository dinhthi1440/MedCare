package com.example.medcare.views.reminder_history.reminder_history_detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentReminderHistoryDetailBinding
import com.example.medcare.extension.changeStatusHistory
import com.example.medcare.models.HistoryStatus
import com.example.medcare.models.Medicine
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import com.example.medcare.views.reminder_history.reminder_history_list.ReminderHistoryViewModel

class ReminderHistoryDetailFragment :
    BaseFragment<FragmentReminderHistoryDetailBinding>(FragmentReminderHistoryDetailBinding::inflate) {
    private val medicineAdapter by lazy { MedicineAdapter(true, ::onclickMedicineItem) }
    private lateinit var historyID: String
    override val viewModel by viewModel<ReminderHistoryViewModel>()


    override fun initData() {
        historyID = arguments?.getString("history_id").toString()
        viewModel.getHistoryDetail(uid, historyID)
    }

    override fun handleEvent() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.txtStatusBtn.setOnClickListener {
            dialog(requireContext()).changeStatusHistory() {
                when (it) {
                    HistoryStatus.DRANK.status -> {
                        viewModel.updateHistory(uid, viewModel.getHistoryDetail.value!!, HistoryStatus.DRANK.status )
                    }
                    HistoryStatus.MISSED.status-> {
                        viewModel.updateHistory(uid, viewModel.getHistoryDetail.value!!, HistoryStatus.DRANK.status )
                    }
                }
            }
        }
    }

    override fun bindData() {
        viewModel.getHistoryDetail.observe(viewLifecycleOwner) { history ->
            binding.apply {
                nestedScrollView.visibility = View.VISIBLE
                txtError.visibility = View.GONE
                txtReminderLabel.text = "\uD83D\uDCCC ${history.label}"
                txtReminderDay.text = "\uD83D\uDCC5 Ngày: ${history.date}"
                txtReminderTime.text = "⏰ Thời gian: ${history.time}"
                txtStatus.text = "\uD83D\uDD04 Trạng thái: ${history.status}"
                txtDisease.text = "\uD83D\uDC89 Bệnh điều trị: ${history.reminder.disease}"
                txtNote.text = history.reminder.note
                rcvMedicineList.layoutManager = LinearLayoutManager(context)
                medicineAdapter.submitList(history.reminder.medicines)
                rcvMedicineList.adapter = medicineAdapter
                if (history.status == HistoryStatus.DRANK.status) {
                    imgCheckSuccess.visibility = View.VISIBLE
                    imgMissing.visibility = View.GONE
                    txtStatusBtn.visibility = View.GONE
                    txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccGreen))
                } else if (history.status == HistoryStatus.MISSED.status) {
                    imgCheckSuccess.visibility = View.GONE
                    imgMissing.visibility = View.VISIBLE
                    txtStatusBtn.visibility = View.GONE
                    txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccRed))
                } else {
                    imgCheckSuccess.visibility = View.GONE
                    imgMissing.visibility = View.GONE
                    txtStatusBtn.visibility = View.VISIBLE
                    txtStatus.setTextColor(ContextCompat.getColor(root.context, R.color.ccBlueText))
                }
            }
        }
        viewModel.getHistoryUpdateStatus.observe(viewLifecycleOwner) {
            viewModel.getHistoryDetail(uid, historyID)
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            binding.txtError.text = it
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

    }
    private fun onclickMedicineItem(medicine: Medicine) {
        val bundle = Bundle().apply {
            putString("medicine_id", medicine.id)
            putString("medicine_name", medicine.name)
            putString("previous_screen", "detail")
        }
        findNavController().navigate(R.id.action_reminderHistoryDetailFragment_to_medicineDetailFragment, bundle)
    }

    override fun destroy() {

    }
}