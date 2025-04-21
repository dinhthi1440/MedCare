package com.example.medcare.views.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.databinding.ActivityAlertBinding
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.Medicine
import com.example.medcare.views.my_medicine.medicine_list.MedicineAdapter

class AlertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlertBinding
    private val medicineAdapter by lazy { MedicineAdapter(true, null) }
    private lateinit var alarmHelper: AlarmHelper
    private val medicines = mutableListOf(
        Medicine("1", "Paracetamol 500mg", "paracetamol.png", "2025-12-31", 23, "viên"),
        Medicine("2", "Amoxicillin 250mg", "amoxicillin.png", "2026-06-15", 10, "viên"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmHelper = AlarmHelper(this)
        binding = ActivityAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rcvMedicineList.layoutManager = LinearLayoutManager(this)
        binding.rcvMedicineList.adapter = medicineAdapter
        medicineAdapter.submitList(medicines.take(2))

        if (medicines.size > 2) {
            binding.txtSeeMore.visibility = View.VISIBLE
        }
        binding.btnCancel.setOnClickListener {
            alarmHelper.stopAlarm()
        }
        binding.btnOk.setOnClickListener {
            alarmHelper.startAlarm()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        alarmHelper.stopAlarm()
    }
}