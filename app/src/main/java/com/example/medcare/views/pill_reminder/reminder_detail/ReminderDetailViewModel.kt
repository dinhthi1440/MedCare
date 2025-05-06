package com.example.medcare.views.pill_reminder.reminder_detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.views.pill_reminder.model.PillReminder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ReminderDetailViewModel(private val iPillReminderRepos: IPillReminderRepos.Local) : BaseViewModel() {

    private lateinit var alarmHelper: AlarmHelper
    private val _setDeleteStatus = MutableLiveData<Boolean>()
    val getDeleteStatus: LiveData<Boolean> get() = _setDeleteStatus

    private val _setReminder = MutableLiveData<PillReminder>()
    val getReminder: LiveData<PillReminder> get() = _setReminder

    fun deleteReminder(pillReminder: PillReminder, context: Context) {
        viewModelScope.launch {
            iPillReminderRepos.deletePillReminder(pillReminder.id)
            alarmHelper = AlarmHelper(context)
            for (time in pillReminder.times) {
                alarmHelper.removeAlarm(context, time.id)
            }
            _setDeleteStatus.value = true
        }
    }

    fun getReminderById(reminderId: String) {
        executeTask(
            request = { iPillReminderRepos.getPillReminderById(reminderId) },
            onSuccess = {
                if (it != null) {
                    _setReminder.value = it
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