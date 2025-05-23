package com.example.medcare.data.datasource.relatives

import com.example.medcare.models.Account
import com.example.medcare.models.Relative
import com.example.medcare.models.Response

interface IRelativesDataSource {
    suspend fun insertRelativesRemote(uid: String, relative: Relative): Response<Any>
    suspend fun getAllRelativesRemote(uid: String): Response<Any>
    suspend fun deleteRelativesRemote(uid: String, idRelative: String): Response<Any>
    //suspend fun getHistoryRelativeByIdRemote(uid: String, idRelative: String): Response<Any>

    suspend fun getSearchRelativesRemote(searchString: String): Response<Any>

    //request
    suspend fun insertRelativesRequestRemote(uid: String, relative: Relative): Response<Any>
    suspend fun getAllRelativeRequestRemote(uid: String): Response<Any>
    suspend fun acceptRelativeRequestRemote(user: Account, relative: Relative): Response<Any>

    //reminder
    suspend fun getAllReminderRelativeFromRemote(uid: String): Response<Any>
}