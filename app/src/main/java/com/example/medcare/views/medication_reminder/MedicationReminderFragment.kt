package com.example.medcare.views.medication_reminder

import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseFragment
import com.example.medcare.databinding.FragmentMedicationReminderBinding
import com.example.medcare.views.medication_reminder.model.PillReminder
import org.koin.androidx.viewmodel.ext.android.viewModel

class MedicationReminderFragment :
    BaseFragment<FragmentMedicationReminderBinding>(FragmentMedicationReminderBinding::inflate) {
    override val viewModel by viewModel<MedicationReminderViewModel>()
    private val reminderAdapter by lazy {
        PillReminderAdapter(
            ::onclickMedicineItem,
            ::onChangeSwitch
        )
    }

    override fun initData() {
        viewModel.getReminderList()
    }

    override fun handleEvent() {

    }

    override fun bindData() {
        viewModel.getReminderList.observe(viewLifecycleOwner) {
            binding.rcvPillReminder.layoutManager = LinearLayoutManager(binding.root.context)
            reminderAdapter.submitList(it)
            binding.rcvPillReminder.adapter = reminderAdapter
        }
    }

    private fun onclickMedicineItem(reminder: PillReminder) {
//        val bundle = Bundle().apply {
//            putString("medicine_id", medicine.id)
//            putString("medicine_name", medicine.name)
//        }
//        findNavController().navigate(R.id.action_myMedicineFragment_to_medicineDetailFragment, bundle)
    }

    private fun onChangeSwitch(id: String, isOn: Boolean) {
        Toast.makeText(this.requireContext(), "id $id là $isOn", Toast.LENGTH_SHORT).show()
    }

    override fun destroy() {

    }
}