package com.example.medcare.views.my_medicine.medicine_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

class MyMedicineViewModel(
    private val iMedicineRepos: IMedicineRepos
) : BaseViewModel() {
    private val _setMedicines = MutableLiveData<Response<Any>>()
    val getMedicines: LiveData<Response<Any>> get() = _setMedicines
    fun getMedicineList(uid: String) {
        executeTask(
            request = { iMedicineRepos.getAllMedicineRemote(uid) },
            onSuccess = {
                _setMedicines.value = it
            },
            onError = {
                _setMedicines.value = Response(
                    500,
                    "Lỗi khi lấy dữ liệu, hãy thử lại",
                    null
                )
            }

        )
    }
}