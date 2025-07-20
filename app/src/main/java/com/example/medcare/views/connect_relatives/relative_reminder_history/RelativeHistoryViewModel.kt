package com.example.medcare.views.connect_relatives.relative_reminder_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.ReminderHistory
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.models.PillReminder
import com.example.medcare.models.Relative

class RelativeHistoryViewModel(private val iRelativeRepos: IRelativeRepos) : BaseViewModel() {

    val getHistoryList: LiveData<MutableList<ReminderHistory>> get() = _setHistoryList
    private val _setHistoryList = MutableLiveData<MutableList<ReminderHistory>>()

    val getUpdateStatus: LiveData<Relative> get() = _setUpdateStatus
    private val _setUpdateStatus = MutableLiveData<Relative>()

    fun getRelativeHistoryByRelativeID(uid: String, relativeID: String) {
        executeTask(
            request = {iRelativeRepos.getRelativeHistoryByRelativeID(uid, relativeID)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val listHistory = it.data as List<ReminderHistory>
                        _setHistoryList.value = listHistory.toMutableList()
                    }
                    204, 403, 404,  500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi không xác định"
            }
        )
    }

    fun updateRelativeHistory(uid: String, relative: Relative) {
        executeTask(
            request = {iRelativeRepos.updateRelativesRemote(uid, relative)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setUpdateStatus.value = relative
                    }
                    404,  500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi không xác định"
            }
        )
    }

}