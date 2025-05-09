package com.example.medcare.views.my_medicine.medicine_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine

class MedicineDetailViewModel(private val iMedicineRepos: IMedicineRepos.Local): BaseViewModel() {
    private val _setMedicine = MutableLiveData<Medicine>()
    val getMedicine: LiveData<Medicine> get() = _setMedicine
    fun getMedicineDetail(medicineId: String) {
        executeTask(
            request = {iMedicineRepos.getMedicineById(medicineId)},
            onSuccess = {
                _setMedicine.value = it
            },
            onError = {}
        )
    }
}