package com.example.medcare.views.my_medicine.medicine_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

class MedicineDetailViewModel(private val iMedicineRepos: IMedicineRepos): BaseViewModel() {
    private val _setMedicine = MutableLiveData<Medicine>()
    val getMedicine: LiveData<Medicine> get() = _setMedicine

    private val _setDeleteStatus = MutableLiveData<Response<Any>>()
    val getDeleteStatus: LiveData<Response<Any>> get() = _setDeleteStatus

    fun getMedicineDetail(uid: String, medicineId: String) {
        executeTask(
            request = {iMedicineRepos.getMedicineByIdRemote(uid, medicineId)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setMedicine.value = it.data as Medicine
                    }
                    404 -> {
                        _messageError.value = it.message
                    }
                    500 -> {
                        _messageError.value = it.message
                    }
                }

            },
            onError = {}
        )
    }
    fun setMedicine(medicine: Medicine){
        _setMedicine.value = medicine
    }
    fun deleteMedicineRemote(uid: String, medicineID: String) {
        executeTask(
            request = {iMedicineRepos.deleteMedicineRemote(uid, medicineID)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setDeleteStatus.value = it
                    }
                    404 -> {
                        _messageError.value = it.message
                    }
                    500 -> {
                        _messageError.value = it.message
                    }
                }

            },
            onError = {}
        )
    }
}