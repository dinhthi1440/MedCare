package com.example.medcare.views.reminder_history.reminder_history_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.reminder_history.IReminderHistoryRepos
import com.example.medcare.models.ReminderHistory

class ReminderHistoryViewModel(private val iReminderHistoryRepos: IReminderHistoryRepos) :
    BaseViewModel() {

    val getHistoryList: LiveData<MutableList<ReminderHistory>> get() = _setHistoryList
    private val _setHistoryList = MutableLiveData<MutableList<ReminderHistory>>()

    val getHistoryDetail: LiveData<ReminderHistory> get() = _setHistoryDetail
    private val _setHistoryDetail = MutableLiveData<ReminderHistory>()

    val getHistoryUpdateStatus: LiveData<String> get() = _setHistoryUpdateStatus
    private val _setHistoryUpdateStatus = MutableLiveData<String>()

    fun getHistoryList(uid: String) {
        executeTask(
            request = { iReminderHistoryRepos.getAllReminderHistory(uid) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val listMedicine = it.data as List<ReminderHistory>
                        _setHistoryList.value = listMedicine.toMutableList()
                    }

                    204, 500 -> {
                        _setHistoryList.value = mutableListOf()
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = it.message
            }
        )
    }

    fun updateHistory(uid: String, history: ReminderHistory, status: String) {
        executeTask(
            request = { iReminderHistoryRepos.updateReminderHistory(uid, history, status) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setHistoryUpdateStatus.value = status
                    }

                    404, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = it.message
            }
        )
    }

    fun getHistoryDetail(uid: String, historyID: String) {
        executeTask(
            request = { iReminderHistoryRepos.getReminderHistoryDetail(uid, historyID) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val historyDetail = it.data as ReminderHistory
                        _setHistoryDetail.value = historyDetail
                    }
                    404, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = it.message
            }
        )
    }

}