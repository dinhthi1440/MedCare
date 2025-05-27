package com.example.medcare.views.pill_reminder

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.R
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentMedicationReminderBinding
import com.example.medcare.models.PillReminder
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicationReminderFragment :
    BaseFragment<FragmentMedicationReminderBinding>(FragmentMedicationReminderBinding::inflate) {
    override val viewModel by viewModel<MedicationReminderViewModel>()
    private val reminderAdapter by lazy {
        PillReminderAdapter(
            ::onclickMedicineItem,
            ::onChangeSwitch,
            ::onRemoveReminder
        )
    }

    override fun initData() {
        viewModel.getReminderList(uid)
    }

    override fun handleEvent() {
        binding.layoutAddNew.setOnClickListener{
            findNavController().navigate(R.id.action_medicationReminderFragment_to_addNewReminderFragment)
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun bindData() {

        viewModel.getReminderList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.txtEmptyList.visibility = View.VISIBLE
                binding.rcvPillReminder.visibility = View.GONE
            } else {
                binding.txtEmptyList.visibility = View.GONE
                binding.rcvPillReminder.visibility = View.VISIBLE
                binding.rcvPillReminder.layoutManager = LinearLayoutManager(binding.root.context)
                reminderAdapter.submitList(it)
                binding.rcvPillReminder.adapter = reminderAdapter
            }
        }
        viewModel.messageError.observe(viewLifecycleOwner) {
            binding.txtEmptyList.text = it
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
        listenBackScreen{
            viewModel.getReminderList(uid)
        }
    }

    private fun onclickMedicineItem(reminder: PillReminder) {
        val bundle = Bundle().apply {
            putString("reminder_id", reminder.id)
        }
        findNavController().navigate(R.id.action_medicationReminderFragment_to_reminderDetailFragment, bundle)
    }

    private fun onChangeSwitch(pillReminder: PillReminder, isOn: Boolean) {
        pillReminder.isOn = isOn
        val updateData = hashMapOf<String, Any>(
            "on" to isOn,
        )
        viewModel.updateFieldsReminder(uid, pillReminder.id, updateData)
    }

    private fun onRemoveReminder(pillReminder: PillReminder) {
        viewModel.deleteReminder(uid, pillReminder, requireContext())
    }

    override fun destroy() {

    }
}