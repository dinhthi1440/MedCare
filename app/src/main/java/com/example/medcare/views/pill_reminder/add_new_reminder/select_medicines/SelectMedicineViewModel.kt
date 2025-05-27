package com.example.medcare.views.pill_reminder.add_new_reminder.select_medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

class SelectMedicineViewModel(private val iMedicineRepos: IMedicineRepos) : BaseViewModel() {
    private val _setSelectedMedicine = MutableLiveData<MutableList<Medicine>>()
    val getSelectedMedicine: LiveData<MutableList<Medicine>> get() = _setSelectedMedicine
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

    fun initData(initSelectedMedicine: MutableSet<Medicine>) {
        _setSelectedMedicine.value = initSelectedMedicine.toMutableList()
    }

    fun selectMedicine(medicine: Medicine) {
        _setSelectedMedicine.value?.add(medicine)
    }

    fun unSelectMedicine(medicine: Medicine) {
        val currentList = _setSelectedMedicine.value ?: mutableListOf()
        val updatedList = currentList.toMutableList().apply {
            removeAll { it.id == medicine.id }
        }
        _setSelectedMedicine.value = updatedList
    }
}