package com.example.medcare.data.repository.doctor

import com.example.medcare.base.DataResult
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.DoctorChat
import com.example.medcare.models.Response

interface IDoctorRepos {
    suspend fun getAllDoctorChat(uid: String): DataResult<Response<Any>>
    suspend fun getSearchDoctorChat(uid: String, search: String) : DataResult<Response<Any>>

    suspend fun getAllDoctor(): DataResult<Response<Any>>
    suspend fun getChatDetail(uid: String, partnerID: String): DataResult<Response<Any>>
    suspend fun insertChatMessage(uid: String, partnerID: String, chatMessage: ChatMessage, doctorChat: DoctorChat): Response<Any>
}