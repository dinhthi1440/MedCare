package com.example.medcare.views.my_medicine.medicine_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.views.my_medicine.medicine_list.model.Medicine

class MyMedicineViewModel: BaseViewModel() {
    private val _getMedicines = MutableLiveData<MutableList<Medicine>>()
    val getMedicines: LiveData<MutableList<Medicine>> get() = _getMedicines
    fun getMedicineList(){
        val medicines = mutableListOf(
            Medicine("1", "Paracetamol 500mg", "paracetamol.png", "2025-12-31", 23, "viên"),
            Medicine("2", "Amoxicillin 250mg", "amoxicillin.png", "2026-06-15", 10, "viên"),
            Medicine("3", "Ibuprofen 400mg", "ibuprofen.png", "2025-09-20", 20, "viên"),
            Medicine("4", "Vitamin C 1000mg", "vitamin_c.png", "2027-03-10", 50, "viên"),
            Medicine("5", "Loratadine 10mg", "loratadine.png", "2025-11-05", 44, "viên")
        )
        _getMedicines.value = medicines
    }
}