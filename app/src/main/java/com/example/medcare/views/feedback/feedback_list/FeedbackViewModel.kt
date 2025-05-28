package com.example.medcare.views.feedback.feedback_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.feedback.IFeedbackRepository
import com.example.medcare.models.Account
import com.example.medcare.models.Feedback
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FeedbackViewModel(private val iFeedbackRepository: IFeedbackRepository) : BaseViewModel() {
    val getFeedbacks: LiveData<MutableList<Feedback>> get() = _setFeedbacks
    private val _setFeedbacks = MutableLiveData<MutableList<Feedback>>()

    val getFeedbackDetail: LiveData<Feedback> get() = _setFeedbackDetail
    private val _setFeedbackDetail = MutableLiveData<Feedback>()

    private val _setUpdateStatus = MutableLiveData<String>()
    val getUpdateStatus: LiveData<String> get() = _setUpdateStatus

    private val _setDeleteStatus = MutableLiveData<String>()
    val getDeleteStatus: LiveData<String> get() = _setDeleteStatus


    fun getFeedbackList(searchString: String) {
        executeTask(
            request = {
                if (searchString.isBlank()){
                    iFeedbackRepository.getAllFeedback()
                } else {
                    iFeedbackRepository.searchFeedback(searchString)
                }
            },
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
                _messageError.value = "Lỗi khi lấy dữ liệu"
            }
        )

    }

    fun getFeedbackByID(feedbackID: String) {
        executeTask(
            request = {iFeedbackRepository.getFeedbackByID(feedbackID)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val feedbackDetail = it.data as Feedback
                        _setFeedbackDetail.value = feedbackDetail
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

    fun updateFeedback(feedbackID: String, fields: Map<String, Any>) {
        executeTask(
            request = {iFeedbackRepository.updateFeedbackByFiled(feedbackID, fields)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _messageError.value = it.message
                        _setUpdateStatus.value = "Success"
                    }
                    else -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Lỗi không xác định"
            }
        )
    }

    fun deleteFeedback(feedbackID: String) {
        executeTask(
            request = {iFeedbackRepository.deleteFeedbackByID(feedbackID)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        _setDeleteStatus.value = "Success"
                    }
                    404 , 500 -> {
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