package com.example.medcare.views.add_new_reminder

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.models.Medicine
import com.example.medcare.views.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.medication_reminder.model.PillReminder
import java.util.UUID

class NewReminderViewModel(private val iPillReminderRepos: IPillReminderRepos) : BaseViewModel() {
    var frequencySelected = FrequencyModel(1, "Hôm nay", true)

    private val _setFrequencyStr = MutableLiveData<String>()
    val getFrequencyStr: LiveData<String> get() = _setFrequencyStr
    private val _setMedicinesSelected = MutableLiveData<MutableList<Medicine>>()
    val listInitialSelected: LiveData<MutableList<Medicine>> get() = _setMedicinesSelected


    fun getData(){
        if (frequencySelected.label == "Tuỳ chỉnh" && frequencySelected.listDateSelected != null) {
            val text = frequencySelected.listDateSelected
                ?.joinToString(separator = ", ") { it.abbreviation }
            _setFrequencyStr.value = text!!
        } else {
            _setFrequencyStr.value = frequencySelected.label
        }
    }
    fun selectFrequency(frequencyModel: FrequencyModel) {
        frequencySelected = frequencyModel
    }
    fun setListSelectedMedicine(listMedicine: MutableList<Medicine>){
        _setMedicinesSelected.value = listMedicine
    }

    fun insertReminder(content: String, note: String, disease: String){
        val reminder = PillReminder(
            UUID.randomUUID().toString(),
            content,
        "",
        "",
        "",
        true, note, disease
        )
    }


}