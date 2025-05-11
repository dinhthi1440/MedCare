package com.example.medcare.views.connect_relatives.relative_reminder_detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.extension.AlarmHelper
import com.example.medcare.models.Medicine
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderRelative
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import kotlinx.coroutines.launch

class RelativeReminderDetailViewModel : BaseViewModel() {
    //    private val _setDeleteStatus = MutableLiveData<Boolean>()
//    val getDeleteStatus: LiveData<Boolean> get() = _setDeleteStatus
    val reminder = ReminderRelative(
        "id1", "user_1723", "Alice", "avatar1.png",
        "Tôi",
        "user_a",
        "Bob",
        "avatarA.png",
        "Bạn bè",
        PillReminder(
            id = "r2",
            label = "Uống thuốc cảm",
            times = listOf(SelectedTime(2, "12:00")),
            frequency = FrequencyModel(2, "Hằng ngày", true),
            medicines = listOf(
                Medicine(id = "m2", name = "Decolgen", dosage = 1, unit = "viên")
            ),
            isOn = true,
            note = "Trước khi ăn trưa",
            disease = "Cảm lạnh"
        )
    )

    private val _setRelativeReminder = MutableLiveData<ReminderRelative>()
    val getRelativeReminder: LiveData<ReminderRelative> get() = _setRelativeReminder

    fun deleteReminder(pillReminder: ReminderRelative, context: Context) {

    }

    fun getReminderById() {
        _setRelativeReminder.value = reminder
//        executeTask(
//            request = { iPillReminderRepos.getPillReminderById(reminderId) },
//            onSuccess = {
//                if (it != null) {
//                    _setReminder.value = it
//                }
//            },
//            onError = {
//
//            }
//        )
    }
}