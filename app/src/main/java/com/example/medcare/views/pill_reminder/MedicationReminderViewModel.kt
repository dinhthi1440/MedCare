package com.example.medcare.views.pill_reminder

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.PillReminder
import com.example.medcare.models.Response
import kotlinx.coroutines.launch

class MedicationReminderViewModel(private val iPillReminderRepos: IPillReminderRepos): BaseViewModel() {
    private val _setReminderList = MutableLiveData<MutableList<PillReminder>>()
    val getReminderList: LiveData<MutableList<PillReminder>> get() = _setReminderList
    private val _setPatchStatus = MutableLiveData<Boolean>()
    val getPatchStatus: LiveData<Boolean> get() = _setPatchStatus
    private lateinit var alarmHelper: AlarmHelper
    fun getReminderList(uid: String) {
        executeTask(
            request = {iPillReminderRepos.getAllPillReminderRemote(uid)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val listMedicine = it.data as List<PillReminder>
                        _setReminderList.value = listMedicine.toMutableList()
                    }
                    204, 500 -> {
                        _setReminderList.value = mutableListOf()
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = it.message
            }
        )
    }

    fun deleteReminder(uid: String, pillReminder: PillReminder, context: Context) {
        viewModelScope.launch {
            val response = iPillReminderRepos.deletePillReminderRemote(uid, pillReminder.id)
            when (response.statusCode) {
                200 -> {
                    alarmHelper = AlarmHelper(context)
                    for (time in pillReminder.times) {
                        alarmHelper.removeAlarm(context, time.id)
                    }
                    val updatedList = getReminderList.value?.toMutableList() ?: mutableListOf()
                    updatedList.remove(pillReminder)
                    _setReminderList.value = updatedList
                }
                404, 500 -> {
                    _messageError.value = response.message
                }
            }
        }
    }

    fun updateFieldsReminder(
        uid: String,
        reminderID: String,
        reminderFields: HashMap<String, Any>
    ) {
        executeTask(
            request = {iPillReminderRepos.updateReminderFieldsRemote(uid, reminderID, reminderFields)},
            onSuccess = {

            },
            onError = {

            }
        )
    }

}