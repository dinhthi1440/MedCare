package com.example.medcare.data.repository.pillreminder

import com.example.medcare.base.DataResult
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderRelative
import com.example.medcare.models.Response

interface IPillReminderRepos {
    suspend fun insertPillReminder(pillReminder: PillReminder): DataResult<Long>
    suspend fun getAllPillReminder(): DataResult<List<PillReminder>>
    suspend fun deletePillReminder(idPillReminder: String): DataResult<Int>
    suspend fun updatePillReminder(pillReminder: PillReminder): DataResult<Int>
    suspend fun getPillReminderById(reminderId: String): DataResult<PillReminder?>

    suspend fun insertPillReminderRemote(uid: String, pillReminder: PillReminder): DataResult<Response<Any>>
    suspend fun getAllPillReminderRemote(uid: String): DataResult<Response<Any>>
    suspend fun deletePillReminderRemote(uid: String, idPillReminder: String): Response<Any>
    suspend fun updatePillReminderRemote(uid: String, pillReminder: PillReminder): Response<Any>
    suspend fun updateReminderFieldsRemote(
        uid: String,
        reminderID: String,
        reminderFields: HashMap<String, Any>
    ): DataResult<Response<Any>>
    suspend fun getPillReminderByIdRemote(uid: String, reminderId: String): DataResult<Response<Any>>

    //relative
    suspend fun insertReminderRelativeRemote(reminderRelative: ReminderRelative): DataResult<Response<Any>>
}