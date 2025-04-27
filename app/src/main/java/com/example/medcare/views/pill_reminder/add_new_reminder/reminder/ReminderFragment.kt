package com.example.medcare.views.pill_reminder.add_new_reminder.reminder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.databinding.FragmentReminderBinding
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.Medicine
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter

class ReminderFragment : Fragment() {

    private lateinit var binding: FragmentReminderBinding
    private val medicineAdapter by lazy { MedicineAdapter(false, null) }

    private val medicines = mutableListOf(
        Medicine("1", "Paracetamol 500mg", "paracetamol.png", "2025-12-31", 23, "viên"),
        Medicine("2", "Amoxicillin 250mg", "amoxicillin.png", "2026-06-15", 10, "viên"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentReminderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rcvMedicineList.layoutManager = LinearLayoutManager(requireContext())
        binding.rcvMedicineList.adapter = medicineAdapter
        medicineAdapter.submitList(medicines.take(2))
        if (medicines.size > 2) {
            binding.txtSeeMore.visibility = View.VISIBLE
        }

        val alarmHelper = AlarmHelper(this.requireContext())
        binding.btnCancel.setOnClickListener {
            alarmHelper.stopAlarm()
        }

        binding.btnOk.setOnClickListener {
            alarmHelper.startAlarm()
        }
    }
}