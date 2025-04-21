package com.example.medcare.views.my_medicine.medicine_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.models.Medicine

class MedicineDetailViewModel: BaseViewModel() {
    private val _getMedicine = MutableLiveData<Medicine>()
    val getMedicine: LiveData<Medicine> get() = _getMedicine
    fun getMedicineDetail() {
        val medicine = Medicine("1", "Paracetamol 500mg", "paracetamol.png", "2025-12-31", 23, "viên")
        _getMedicine.value = medicine
    }
}