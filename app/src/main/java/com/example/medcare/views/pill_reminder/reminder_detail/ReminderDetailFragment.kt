package com.example.medcare.views.pill_reminder.reminder_detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentReminderDetailBinding
import com.example.medcare.extension.confirmEvent
import com.example.medcare.models.Medicine
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter
import com.example.medcare.views.pill_reminder.model.PillReminder
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderDetailFragment : BaseFragment<FragmentReminderDetailBinding>(FragmentReminderDetailBinding::inflate) {
    override val viewModel by viewModel<ReminderDetailViewModel>()
    private val medicineAdapter by lazy { MedicineAdapter(true, ::onclickMedicineItem) }
    private var reminderId = ""
    override fun initData() {
        reminderId = arguments?.getString("reminder_id").toString()

    }

    override fun handleEvent() {
        binding.apply {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnDelete.setOnClickListener {
                dialog(requireContext()).confirmEvent("Xác nhận xoá", "Bạn có chắc chắn muốn xoá?") {
                    if (viewModel.getReminder.value != null) {
                        viewModel.deleteReminder(viewModel.getReminder.value!!, requireContext())
                    }
                }
            }
            layoutEditReminder.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("reminder_id", viewModel.getReminder.value?.id)
                }
                findNavController().navigate(R.id.action_reminderDetailFragment_to_addNewReminderFragment, bundle)
            }
            swtOn.setOnCheckedChangeListener { _, isOn ->
                onChangeSwitch(isOn)
            }
        }
    }
    private fun onChangeSwitch(isOn: Boolean) {
        val pillReminder = viewModel.getReminder.value
        if (pillReminder != null) {
            pillReminder.isOn = isOn
            viewModel.updateReminder(pillReminder)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun bindData() {
        viewModel.getReminderById(reminderId)
        viewModel.getReminder.observe(viewLifecycleOwner) {reminder ->
            binding.apply {
                txtReminderLabel.text = "\uD83D\uDCCC ${reminder.label}"
                txtReminderFrequency.text = "\uD83D\uDD04 Tần suất: ${reminder.frequency.label}"
                val timeString = reminder.times.joinToString(", ") { it.time}
                txtReminderTime.text = "⏰ Thời gian: $timeString"
                txtDisease.text = "\uD83D\uDC89 Bệnh điều trị: ${reminder.disease}"
                txtNote.text = reminder.note
                swtOn.isChecked = reminder.isOn
                rcvMedicineList.layoutManager = LinearLayoutManager(context)
                medicineAdapter.submitList(reminder.medicines)
                rcvMedicineList.adapter = medicineAdapter
            }
        }
        viewModel.getDeleteStatus.observe(viewLifecycleOwner) {
            if (it){
                val result = Bundle().apply {
                    putBoolean("key_boolean", true)
                }
                parentFragmentManager.setFragmentResult("boolean_result_key", result)
                findNavController().popBackStack()
            }
        }
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