package com.example.medcare.views.my_medicine.add_medicine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine

class AddMedicineViewModel(private val iMedicineRepos: IMedicineRepos.Local) : BaseViewModel() {

    private val _setInsertStatus = MutableLiveData<Boolean> ()
    val  getInsertStatus: LiveData<Boolean> get() = _setInsertStatus
    fun insertMedicine(medicine: Medicine){
        executeTask(
            request = {iMedicineRepos.insertMedicine(medicine)},
            onSuccess = {
                _setInsertStatus.value = (it>=0)
            },
            onError = {
                _setInsertStatus.value = false
                Log.e("TAG", "insertMedicine: lỗi gì ? ${it}", )
            }
        )
    }

    fun updateMedicine(medicine: Medicine) {
        executeTask(
            request = {iMedicineRepos.updateMedicine(medicine)},
            onSuccess = {
                _setInsertStatus.value = (it>=0)
            },
            onError = {
                _setInsertStatus.value = false
                Log.e("TAG", "updateMedicine: lỗi gì ? ${it}", )
            }
        )
    }

}