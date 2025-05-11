package com.example.medcare.views.pill_reminder

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.PillReminder
import kotlinx.coroutines.launch

class MedicationReminderViewModel(private val iPillReminderRepos: IPillReminderRepos.Local): BaseViewModel() {
    private val _setReminderList = MutableLiveData<MutableList<PillReminder>>()
    val getReminderList: LiveData<MutableList<PillReminder>> get() = _setReminderList
    private val _setPatchStatus = MutableLiveData<Boolean>()
    val getPatchStatus: LiveData<Boolean> get() = _setPatchStatus
    private lateinit var alarmHelper: AlarmHelper
    fun getReminderList() {
        executeTask(
            request = {iPillReminderRepos.getAllPillReminder()},
            onSuccess = {
                _setReminderList.value = it.toMutableList()
                _setPatchStatus.value = true
            },
            onError = {
                _setPatchStatus.value = false
            }
        )
    }

    fun deleteReminder(pillReminder: PillReminder, context: Context) {
        viewModelScope.launch {
            iPillReminderRepos.deletePillReminder(pillReminder.id)
            alarmHelper = AlarmHelper(context)
            for (time in pillReminder.times) {
                alarmHelper.removeAlarm(context, time.id)
            }
            val updatedList = getReminderList.value?.toMutableList() ?: mutableListOf()
            updatedList.remove(pillReminder)
            _setReminderList.value = updatedList
        }
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