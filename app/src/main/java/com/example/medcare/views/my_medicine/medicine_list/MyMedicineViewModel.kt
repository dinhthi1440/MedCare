package com.example.medcare.views.my_medicine.medicine_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine

class MyMedicineViewModel(private val iMedicineRepos: IMedicineRepos.Local): BaseViewModel() {
    private val _getMedicines = MutableLiveData<MutableList<Medicine>>()
    val getMedicines: LiveData<MutableList<Medicine>> get() = _getMedicines
    fun getMedicineList(){
        executeTask(
            request = {iMedicineRepos.getAllMedicine()},
            onSuccess = {
                _getMedicines.value = it.toMutableList()
            },
            onError = {

            }

        )
    }
}