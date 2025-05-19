package com.example.medcare.views.my_medicine.add_medicine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

class AddMedicineViewModel(private val iMedicineRepos: IMedicineRepos) : BaseViewModel() {

    private val _setInsertStatus = MutableLiveData<Response<Any>> ()
    val  getInsertStatus: LiveData<Response<Any>> get() = _setInsertStatus
    fun insertMedicine(uid: String, medicine: Medicine){
        executeTask(
            request = {iMedicineRepos.insertMedicineRemote(uid, medicine)},
            onSuccess = {
                _setInsertStatus.value = it
            },
            onError = {
                _setInsertStatus.value = Response(
                    500,
                    "Lỗi khi tạo dữ liệu, hãy thử lại",
                    null
                )
            }
        )
    }

    fun updateMedicine(uid: String, medicine: Medicine) {
        executeTask(
            request = {iMedicineRepos.updateMedicineRemote(uid, medicine)},
            onSuccess = {
                _setInsertStatus.value = it
            },
            onError = {
                _setInsertStatus.value = Response(
                    500,
                    "Lỗi khi cập nhật dữ liệu, hãy thử lại",
                    null
                )
            }
        )
    }

}