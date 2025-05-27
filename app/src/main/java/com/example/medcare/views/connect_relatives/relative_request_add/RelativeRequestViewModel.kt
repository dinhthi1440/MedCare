package com.example.medcare.views.connect_relatives.relative_request_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.models.PillReminder
import com.example.medcare.models.PillReminderResult

class RelativeRequestViewModel(private val iRelativeRepos: IRelativeRepos) : BaseViewModel() {

    val getReminderFrom: LiveData<MutableList<PillReminder>> get() = _setReminderFrom
    private val _setReminderFrom = MutableLiveData<MutableList<PillReminder>>()
    val getReminderTo: LiveData<MutableList<PillReminder>> get() = _setReminderTo
    private val _setReminderTo = MutableLiveData<MutableList<PillReminder>>()



    fun getData(uid: String) {
        executeTask(
            request = {iRelativeRepos.getAllReminderRelativeFromRemote(uid)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val result = it.data as PillReminderResult
                        _setReminderTo.value = result.listSendTo.toMutableList()
                        _setReminderFrom.value = result.listSendFrom.toMutableList()
                    }
                    204 -> {
                        _setReminderTo.value = mutableListOf()
                        _setReminderFrom.value = mutableListOf()
                    }
                    500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi lấy dữ liệu"
            }
        )
    }



}