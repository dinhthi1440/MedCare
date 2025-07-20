package com.example.medcare.views.connect_relatives.relative_reminder_detail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.models.PillReminder
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class RelativeReminderDetailViewModel(private val iRelativeRepos: IRelativeRepos) : BaseViewModel() {
    //    private val _setDeleteStatus = MutableLiveData<Boolean>()
//    val getDeleteStatus: LiveData<Boolean> get() = _setDeleteStatus


    private val _setRelativeReminder = MutableLiveData<PillReminder>()
    val getRelativeReminder: LiveData<PillReminder> get() = _setRelativeReminder

    val getStatusUpdate: LiveData<String> get() = _setStatusUpdate
    private val _setStatusUpdate = MutableLiveData<String>()

    val getDeleteStatus: LiveData<Boolean> get() = _setDeleteStatus
    private val _setDeleteStatus = MutableLiveData<Boolean>()

    fun deleteReminder(pillReminder: PillReminder) {
        executeTask(
            request = { iRelativeRepos.deleteRelativeReminderRemote(pillReminder.senderID, pillReminder.receiverID, pillReminder.id) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setDeleteStatus.value = true
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

    fun getReminderById(uid: String, reminderID: String) {
        executeTask(
            request = { iRelativeRepos.getPillReminderByIdRemote(uid, reminderID) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setRelativeReminder.value = it.data as PillReminder
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
    fun updateReminder(relativeReminder: PillReminder) {
        executeTask(
            request = {iRelativeRepos.updateReminderRelativeFromToRemote(relativeReminder)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setStatusUpdate.value = relativeReminder.statusRequest ?: ""
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
    fun insertReminder(relativeReminder: PillReminder) {
        viewModelScope.launch{
            iRelativeRepos.insertPillReminder(relativeReminder)
        }

    }
}