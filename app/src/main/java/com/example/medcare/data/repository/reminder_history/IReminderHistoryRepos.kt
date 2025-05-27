package com.example.medcare.data.repository.reminder_history

import com.example.medcare.base.DataResult
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response

interface IReminderHistoryRepos {
    suspend fun insertReminderHistory(uid: String, reminderHistory: ReminderHistory): DataResult<Response<Any>>
    suspend fun getAllReminderHistory(uid: String): DataResult<Response<Any>>
    suspend fun updateReminderHistory(uid: String, history: ReminderHistory, status: String): DataResult<Response<Any>>
    suspend fun getReminderHistoryDetail(uid: String, historyID: String, ): DataResult<Response<Any>>
}