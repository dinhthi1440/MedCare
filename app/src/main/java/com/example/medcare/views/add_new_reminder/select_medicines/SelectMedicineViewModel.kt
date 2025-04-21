package com.example.medcare.views.add_new_reminder.select_medicines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine

class SelectMedicineViewModel(private val iMedicineRepos: IMedicineRepos.Local) : BaseViewModel() {
    private val _setMedicines = MutableLiveData<MutableList<Medicine>>()
    val getMedicines: LiveData<MutableList<Medicine>> get() = _setMedicines
    private val _setSelectedMedicine = MutableLiveData<MutableList<Medicine>>()
    val getSelectedMedicine: LiveData<MutableList<Medicine>> get() = _setSelectedMedicine
    fun getMedicineList(){
        executeTask(
            request =  {iMedicineRepos.getAllMedicine()},
            onSuccess = {
                _setMedicines.value = it.toMutableList()
            },
            onError = {

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