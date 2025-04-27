package com.example.medcare.views.pill_reminder.add_new_reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.Medicine
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.views.pill_reminder.model.PillReminder
import java.util.UUID

class NewReminderViewModel(private val iPillReminderRepos: IPillReminderRepos.Local) : BaseViewModel() {
    var frequencySelected = FrequencyModel(1, "Hôm nay", true)

    private val _setFrequencyStr = MutableLiveData<String>()
    val getFrequencyStr: LiveData<String> get() = _setFrequencyStr
    private val _setMedicinesSelected = MutableLiveData<MutableList<Medicine>>()
    val listInitialSelected: LiveData<MutableList<Medicine>> get() = _setMedicinesSelected

    private val _setInsertStatus = MutableLiveData<Boolean>()
    val getInsertStatus: LiveData<Boolean> get() = _setInsertStatus
    private val _setPillReminder = MutableLiveData<PillReminder>()
    val getPillReminder: LiveData<PillReminder> get() = _setPillReminder

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

    fun insertReminder(
        times: List<SelectedTime>,
        content: String,
        note: String,
        disease: String
    ) {
        val reminder = PillReminder(
            UUID.randomUUID().toString(),
            content,
            times,
            frequencySelected,
            listInitialSelected.value?.toList() ?: listOf(),
            true, note, disease
        )
        executeTask(
            request = {iPillReminderRepos.insertPillReminder(reminder)},
            onSuccess = {
                _setInsertStatus.value = (it>=0)
                _setPillReminder.value = reminder
            },
            onError = {
                _setInsertStatus.value = false
            }
        )
    }

    fun getReminderById(reminderId: String) {
        executeTask(
            request = { iPillReminderRepos.getPillReminderById(reminderId) },
            onSuccess = {
                if (it != null) {
                    _setPillReminder.value = it
                }
            },
            onError = {

            }
        )
    }

    fun updateReminder(pillReminder: PillReminder){
        executeTask(
            request = {iPillReminderRepos.updatePillReminder(pillReminder)},
            onSuccess = {

            },
            onError = {

            }
        )
    }


}