package com.example.medcare.views.pill_reminder.add_new_reminder

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.Medicine
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderRelative
import kotlinx.coroutines.launch
import java.util.UUID

class NewReminderViewModel(
    private val iPillReminderRepos: IPillReminderRepos
) : BaseViewModel() {
    var frequencySelected = FrequencyModel(1, "Hôm nay", true)

    private val _setFrequencyStr = MutableLiveData<String>()
    val getFrequencyStr: LiveData<String> get() = _setFrequencyStr
    private val _setMedicinesSelected = MutableLiveData<MutableList<Medicine>>()
    val listInitialSelected: LiveData<MutableList<Medicine>> get() = _setMedicinesSelected

    private val _setInsertStatus = MutableLiveData<Boolean>()
    val getInsertStatus: LiveData<Boolean> get() = _setInsertStatus
    private val _setPillReminder = MutableLiveData<PillReminder>()
    val getPillReminder: LiveData<PillReminder> get() = _setPillReminder

    private val _setUpdateStatus = MutableLiveData<Boolean>()
    val getUpdateStatus: LiveData<Boolean> get() = _setUpdateStatus

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
        uid: String,
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
            request = {iPillReminderRepos.insertPillReminderRemote(uid, reminder)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setPillReminder.value = reminder
                    }
                    500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi không xác định"
            }
        )
    }

    fun getReminderByIdRemote(uid: String, reminderId: String) {
        executeTask(
            request = { iPillReminderRepos.getPillReminderByIdRemote(uid, reminderId) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setPillReminder.value = it.data as PillReminder
                    }
                    404, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {

            }
        )
    }

    fun updateReminder(uid: String, pillReminder: PillReminder, alarmHelper: AlarmHelper, context: Context){
        viewModelScope.launch {
            val oldSelectedTimes = getPillReminder.value?.times ?: mutableListOf()
            val newSelectedTimes = pillReminder.times
            val timesToDelete = oldSelectedTimes.filter { oldTime ->
                newSelectedTimes.none { newTime -> newTime.id == oldTime.id && newTime.time == oldTime.time }
            }

            val timesToAdd = newSelectedTimes.filter { newTime ->
                oldSelectedTimes.none { oldTime -> oldTime.id == newTime.id && oldTime.time == newTime.time }
            }

            // Xoá các alarm cũ
            timesToDelete.forEach { oldTime ->
                Log.e("TAG", "updateReminder: đã xoá là ${oldTime.time}", )
                context.let { it1 -> alarmHelper.removeAlarm(it1, oldTime.id) }
            }

            // Tạo alarm mới cho các newTime

            if (timesToAdd.isNotEmpty()) {
                Log.e("TAG", "updateReminder: mới là là ${timesToAdd.size}", )
                val reminder = PillReminder(
                    UUID.randomUUID().toString(),
                    "",
                    timesToAdd,
                    frequencySelected,
                    listOf(),
                    true, "", ""
                )
                context.let { it1 -> alarmHelper.registerAlarm(it1, reminder) }
            }
            val response = iPillReminderRepos.updatePillReminderRemote(uid, pillReminder)
            when (response.statusCode) {
                200 -> {
                    _setUpdateStatus.value = true
                }
                404, 500 -> {
                    _messageError.value = response.message
                }
            }

        }

    }

    fun insertReminderRelativeRemote(
        times: List<SelectedTime>,
        content: String,
        note: String,
        disease: String,
        senderID: String,
        senderName: String,
        senderAvatar: String,
        senderDescription: String,
        receiverID: String,
        receiverName: String,
        receiverAvatar: String,
        receiverDescription: String
    ) {
        val reminder = PillReminder(
            UUID.randomUUID().toString(),
            content,
            times,
            frequencySelected,
            listInitialSelected.value?.toList() ?: listOf(),
            true, note, disease
        )
        val reminderRelative = ReminderRelative(
            reminder.id, senderID, senderName, senderAvatar, senderDescription,
            receiverID, receiverName, receiverAvatar, receiverDescription, reminder
        )
        executeTask(
            request = { iPillReminderRepos.insertReminderRelativeRemote(reminderRelative) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setPillReminder.value = reminderRelative.pillReminder
                    }

                    500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {

            }
        )
    }
}