package com.example.medcare.data.datasource.pillreminder

import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderRelative
import com.example.medcare.models.Response

interface IPillReminderDataSource {
    suspend fun insertPillReminder(pillReminder: PillReminder): Long
    suspend fun getAllPillReminder(): List<PillReminder>
    suspend fun deletePillReminder(idPillReminder: String): Int
    suspend fun updatePillReminder(pillReminder: PillReminder): Int
    suspend fun getPillReminderById(reminderId: String): PillReminder?

    suspend fun insertPillReminderRemote(uid: String, pillReminder: PillReminder): Response<Any>
    suspend fun getAllPillReminderRemote(uid: String): Response<Any>
    suspend fun deletePillReminderRemote(uid: String, idPillReminder: String): Response<Any>
    suspend fun updatePillReminderRemote(uid: String, pillReminder: PillReminder): Response<Any>
    suspend fun updateReminderFieldsRemote(
        uid: String,
        reminderID: String,
        reminderFields: HashMap<String, Any>
    ): Response<Any>

    suspend fun getPillReminderByIdRemote(uid: String, reminderId: String): Response<Any>

    //relative
    suspend fun insertReminderRelativeRemote(reminderRelative: ReminderRelative): Response<Any>

}