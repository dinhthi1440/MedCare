package com.example.medcare.views.health_advice.contact_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.doctor.IDoctorRepos
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.Doctor
import com.example.medcare.models.DoctorChat

class ContactDoctorViewModel(private val iDoctorRepos: IDoctorRepos) : BaseViewModel(){


    val getDoctorChats: LiveData<MutableList<DoctorChat>> get() = _setDoctorChats
    private val _setDoctorChats = MutableLiveData<MutableList<DoctorChat>>()



    fun getChatList(uid: String) {

        executeTask(
            request = {iDoctorRepos.getAllDoctorChat(uid)},
            onSuccess = {
                when (it.statusCode) {
                    200 -> {
                        val listDoctorChat = it.data as List<DoctorChat>
                        if (listDoctorChat.isEmpty()) {
                            _messageError.value = "Bạn chưa có cuộc tư vấn nào!"
                        } else {
                            _setDoctorChats.value = listDoctorChat.toMutableList()
                        }
                    }
                    500 -> {
                        _messageError.value = it.message
                    }
                }
            },
            onError = {
                _messageError.value = "Có lỗi khi lấy dữ liệu"
            }
        )
    }



}