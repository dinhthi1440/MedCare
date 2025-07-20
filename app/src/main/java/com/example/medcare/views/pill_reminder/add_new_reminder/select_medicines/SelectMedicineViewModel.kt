package com.example.medcare.views.pill_reminder.add_new_reminder.select_medicines

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

class SelectMedicineViewModel(private val iMedicineRepos: IMedicineRepos) : BaseViewModel() {
    private val _setSelectedMedicine = MutableLiveData<MutableList<Medicine>>()
    val getSelectedMedicine: LiveData<MutableList<Medicine>> get() = _setSelectedMedicine
    private val _setMedicines = MutableLiveData<MutableList<Medicine>>()
    val getMedicines: LiveData<MutableList<Medicine>> get() = _setMedicines
    fun getMedicineList(uid: String) {
        executeTask(
            request = { iMedicineRepos.getAllMedicineRemote(uid) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val fetchedMedicines = (it.data as? List<Medicine>)?.toMutableList() ?: mutableListOf()
                        val currentSelectedMedicines = _setSelectedMedicine.value ?: mutableListOf()
                        val combinedList = fetchedMedicines
                        for (selectedMed in currentSelectedMedicines) {
                            if (combinedList.none { it.id == selectedMed.id }) {
                                combinedList.add(selectedMed)
                            }
                        }
                        _setMedicines.value = combinedList
                    }
                    204, 500 -> {
                        _setMedicines.value = mutableListOf()
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi không xác định"
            }

        )
    }

    fun initData(initSelectedMedicine: MutableSet<Medicine>) {
        _setSelectedMedicine.value = initSelectedMedicine.toMutableList()
    }

    fun addMedicineToList(newMedicine: Medicine) {
        val currentSelectedList = getMedicines.value?.toMutableList() ?: mutableListOf()
        currentSelectedList.add(newMedicine)
        _setMedicines.value = currentSelectedList

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