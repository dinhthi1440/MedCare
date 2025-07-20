package com.example.medcare.views.setting.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.feedback.IFeedbackRepository
import com.example.medcare.models.Feedback

class SettingFeedbackViewModel(private val iFeedbackRepository: IFeedbackRepository) : BaseViewModel() {
    private val _setFeedbacks = MutableLiveData<MutableList<Feedback>>()
    val getFeedbacks: LiveData<MutableList<Feedback>> get() = _setFeedbacks

    private val _setInsertStatus = MutableLiveData<String>()
    val getInsertStatus: LiveData<String> get() = _setInsertStatus

    fun getFeedbackList(uid: String) {
        executeTask(
            request = { iFeedbackRepository.getFeedbackByUserID(uid) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val listFeedback = it.data as List<Feedback>
                        _setFeedbacks.value = listFeedback.toMutableList()
                    }
                    204, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi không xác định"
            }
        )
    }

    fun insertFeedback(feedback: Feedback) {
        executeTask(
            request = { iFeedbackRepository.insertFeedback(feedback) },
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setInsertStatus.value = it.message
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
}