package com.example.medcare.data.datasource.relatives

import com.example.medcare.models.Account
import com.example.medcare.models.PillReminder
import com.example.medcare.models.Relative
import com.example.medcare.models.Response

interface IRelativesDataSource {
    suspend fun insertRelativesRemote(uid: String, relative: Relative): Response<Any>
    suspend fun updateRelativesRemote(uid: String, relative: Relative): Response<Any>
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
    suspend fun updateReminderRelativeFromToRemote(pillReminder: PillReminder): Response<Any>
    suspend fun getReminderRelativeFromToByID(uid: String, reminderID: String): Response<Any>
    suspend fun getRelativeHistoryByRelativeID(uid: String, relativeID: String): Response<Any>
    suspend fun deleteRelativeReminderRemote(uid: String, idRelative: String, idReminder: String): Response<Any>

    suspend fun insertPillReminder(pillReminder: PillReminder): Long

}