package com.example.medcare.views.pill_reminder.reminder_detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.HistoryStatus
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderHistory
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

class ReminderDetailViewModel(private val iPillReminderRepos: IPillReminderRepos) : BaseViewModel() {

    private lateinit var alarmHelper: AlarmHelper
    private val _setDeleteStatus = MutableLiveData<Boolean>()
    val getDeleteStatus: LiveData<Boolean> get() = _setDeleteStatus

    private val _setReminder = MutableLiveData<PillReminder>()
    val getReminder: LiveData<PillReminder> get() = _setReminder

    fun deleteReminder(uid: String, pillReminder: PillReminder, context: Context) {
        viewModelScope.launch {
            val response = iPillReminderRepos.deletePillReminderRemote(uid, pillReminder.id)
            when (response.statusCode) {
                200 -> {
                    alarmHelper = AlarmHelper(context)
                    for (time in pillReminder.times) {
                        alarmHelper.removeAlarm(context, time.id)
                    }
                    _setDeleteStatus.value = true
                }
                404, 500 -> {
                    _messageError.value = response.message
                }
            }
        }
    }

    fun getReminderById(uid: String, reminderId: String) {
        executeTask(
            request = { iPillReminderRepos.getPillReminderByIdRemote(uid, reminderId) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setReminder.value = it.data as PillReminder
                    }
                    404, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi không xác định, vui lòng thử lại"
            }
        )
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

    fun insertHistory(uid: String, reminder: PillReminder) {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val todayString = today.format(formatter)
        val history = ReminderHistory(
            UUID.randomUUID().toString(),
            reminder.label,
            todayString,
            reminder.times.first().time,
            HistoryStatus.NOT_CONFIRMED.status,
            reminder
        )
        executeTask(
            request = {iPillReminderRepos.insertReminderHistory(uid, history)},
            onSuccess = {
                _messageError.value = it.message
            },
            onError = {
                _messageError.value = "Lỗi"
            }
        )
    }
}