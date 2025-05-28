package com.example.medcare.views.health_advice.chat_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.doctor.IDoctorRepos
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.Doctor
import com.example.medcare.models.DoctorChat
import com.example.medcare.models.Response
import kotlinx.coroutines.launch

class ChatDoctorViewModel(private val iDoctorRepos: IDoctorRepos) : BaseViewModel() {

    val getDoctors: LiveData<MutableList<Doctor>> get() = _setDoctors
    private val _setDoctors = MutableLiveData<MutableList<Doctor>>()

    val getChatDetail: LiveData<MutableList<ChatMessage>> get() = _setChatDetail
    private val _setChatDetail = MutableLiveData<MutableList<ChatMessage>>()

    val getInsertStatus: LiveData<String> get() = _setInsertStatus
    private val _setInsertStatus = MutableLiveData<String>()

    fun getSearchDoctorChatList(searchString: String ) {

    }

    fun getAllDoctorList() {
        executeTask(
            request = {iDoctorRepos.getAllDoctor()},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val listDoctor = it.data as List<Doctor>
                        if (listDoctor.isEmpty()) {
                            _messageError.value = "Không có bác sĩ nào trên hệ thống"
                        } else {
                            _setDoctors.value = listDoctor.toMutableList()
                        }
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

    fun getChatDetail(uid: String, doctorID: String) {
        executeTask(
            request = {iDoctorRepos.getChatDetail(uid, doctorID)},
            onSuccess = {
                when(it.statusCode) {
                    200 -> {
                        val chatList = it.data as List<ChatMessage>
                        _setChatDetail.value = chatList.toMutableList()
                    }
                    204, 500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Có lỗi khi lấy dữ liệu tin nhắn"
            }
        )
    }

    fun addChatMessage(newMessage: ChatMessage) {
        val currentList = _setChatDetail.value ?: mutableListOf()
        currentList.add(newMessage)
        _setChatDetail.value = currentList
    }

    fun insertChatMessage(uid: String, partnerID: String, chatMessage: ChatMessage, doctorChat: DoctorChat) {
        viewModelScope.launch {
            val result = iDoctorRepos.insertChatMessage(uid, partnerID, chatMessage, doctorChat)
            when (result.statusCode) {
                200 -> {
                    _setInsertStatus.value = result.message
                }
                500 -> {
                    _messageError.value = result.message
                }
            }
        }
    }


}