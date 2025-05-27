package com.example.medcare.data.repository.doctor

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.doctor.IDoctorDatasource
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.DoctorChat
import com.example.medcare.models.Response

class DoctorRepos(private val iDoctorDatasource: IDoctorDatasource) : BaseRepository(), IDoctorRepos {
    override suspend fun getAllDoctorChat(uid: String): DataResult<Response<Any>> {
        return getResult { iDoctorDatasource.getAllDoctorChat(uid) }
    }

    override suspend fun getSearchDoctorChat(
        uid: String,
        search: String
    ): DataResult<Response<Any>> {
        return getResult { iDoctorDatasource.getSearchDoctorChat(uid, search) }
    }

    override suspend fun getAllDoctor(): DataResult<Response<Any>> {
        return getResult { iDoctorDatasource.getAllDoctor() }
    }

    override suspend fun getChatDetail(uid: String, partnerID: String): DataResult<Response<Any>> {
        return getResult { iDoctorDatasource.getChatDetail(uid, partnerID) }
    }

    override suspend fun insertChatMessage(
        uid: String,
        partnerID: String,
        chatMessage: ChatMessage,
        doctorChat: DoctorChat
    ): Response<Any> {
        return iDoctorDatasource.insertChatMessage(uid, partnerID, chatMessage, doctorChat)
    }

}