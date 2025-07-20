package com.example.medcare.data.datasource.reminder_history

import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response

interface IReminderHistoryDatasource {
    suspend fun insertReminderHistory(uid: String, reminderHistory: ReminderHistory): Response<Any>
    suspend fun getAllReminderHistory(uid: String): Response<Any>
    suspend fun updateReminderHistory(uid: String, history: ReminderHistory, status: String): Response<Any>
    suspend fun getReminderHistoryDetail(uid: String, historyID: String,): Response<Any>

}