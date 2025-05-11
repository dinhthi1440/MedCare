package com.example.medcare.views.connect_relatives.relative_reminder_detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentRelativeReminderDetailBinding
import com.example.medcare.extension.confirmEvent
import com.example.medcare.models.Medicine
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class RelativeReminderDetailFragment : BaseFragment<FragmentRelativeReminderDetailBinding>(FragmentRelativeReminderDetailBinding::inflate) {
    private val medicineAdapter by lazy { MedicineAdapter(true, ::onclickMedicineItem) }
    override val viewModel by viewModel<RelativeReminderDetailViewModel>()

    override fun initData() {
        viewModel.getReminderById()
    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
//            btnDelete.setOnClickListener {
//                dialog(requireContext()).confirmEvent("Xác nhận xoá", "Bạn có chắc chắn muốn xoá?") {
//                    if (viewModel.getReminder.value != null) {
//                        viewModel.deleteReminder(viewModel.getReminder.value!!, requireContext())
//                    }
//                }
//            }
//            layoutEditReminder.setOnClickListener {
//                val bundle = Bundle().apply {
//                    putString("reminder_id", viewModel.getReminder.value?.id)
//                }
//                findNavController().navigate(R.id.action_reminderDetailFragment_to_addNewReminderFragment, bundle)
//            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        var myID = "user_123"
        viewModel.getRelativeReminder.observe(viewLifecycleOwner) {reminder ->
            binding.apply {
                if (reminder.senderID == myID) {
                    txtFromOrTo.text = "Tới: "
                    txtRelativeName.text = reminder.receiverName
                    txtDescription.text = reminder.receiverDescription
                    btnConfirm.visibility = View.GONE
                    btnDefuse.visibility = View.GONE
                    btnDelete.visibility = View.VISIBLE
                } else {
                    txtFromOrTo.text = "Tạo bởi: "
                    txtRelativeName.text = reminder.senderName
                    txtDescription.text = reminder.senderDescription
                    btnConfirm.visibility = View.VISIBLE
                    btnDefuse.visibility = View.VISIBLE
                    btnDelete.visibility = View.GONE
                }
                txtReminderLabel.text = "\uD83D\uDCCC ${reminder.pillReminder.label}"
                txtReminderFrequency.text = "\uD83D\uDD04 Tần suất: ${reminder.pillReminder.frequency.label}"
                val timeString = reminder.pillReminder.times.joinToString(", ") { it.time}
                txtReminderTime.text = "⏰ Thời gian: $timeString"
                txtDisease.text = "\uD83D\uDC89 Bệnh điều trị: ${reminder.pillReminder.disease}"
                txtNote.text = reminder.pillReminder.note
                rcvMedicineList.layoutManager = LinearLayoutManager(context)
                medicineAdapter.submitList(reminder.pillReminder.medicines)
                rcvMedicineList.adapter = medicineAdapter
            }
        }
//        viewModel.getDeleteStatus.observe(viewLifecycleOwner) {
//            if (it){
//                val result = Bundle().apply {
//                    putBoolean("key_boolean", true)
//                }
//                parentFragmentManager.setFragmentResult("boolean_result_key", result)
//                findNavController().popBackStack()
//            }
//        }
    }
    private fun onclickMedicineItem(medicine: Medicine) {
        val bundle = Bundle().apply {
            putString("medicine_id", medicine.id)
            putString("medicine_name", medicine.name)
            putString("previous_screen", "detail")
        }
        findNavController().navigate(R.id.action_reminderDetailFragment_to_medicineDetailFragment, bundle)
    }

    override fun destroy() {

    }
}