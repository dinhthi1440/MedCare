package com.example.medcare.data.datasource.doctor

import com.example.medcare.models.ChatMessage
import com.example.medcare.models.DoctorChat
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response

interface IDoctorDatasource {
    //suspend fun insertChat(uid: String, reminderHistory: ReminderHistory): Response<Any>
    suspend fun getAllDoctorChat(uid: String): Response<Any>

    suspend fun getSearchDoctorChat(uid: String, search: String) : Response<Any>

    suspend fun getAllDoctor(): Response<Any>

    suspend fun getChatDetail(uid: String, partnerID: String): Response<Any>

    suspend fun insertChatMessage(uid: String, partnerID: String, chatMessage: ChatMessage, doctorChat: DoctorChat): Response<Any>
}