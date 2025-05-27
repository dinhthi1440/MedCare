package com.example.medcare.data.repository.reminder_history

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.reminder_history.IReminderHistoryDatasource
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response

class ReminderHistoryRepos(private val iReminderHistoryDatasource: IReminderHistoryDatasource) :
    BaseRepository(), IReminderHistoryRepos {
    override suspend fun insertReminderHistory(
        uid: String,
        reminderHistory: ReminderHistory
    ): DataResult<Response<Any>> {
        return getResult { iReminderHistoryDatasource.insertReminderHistory(uid, reminderHistory) }
    }

    override suspend fun getAllReminderHistory(uid: String): DataResult<Response<Any>> {
        return getResult { iReminderHistoryDatasource.getAllReminderHistory(uid) }
    }

    override suspend fun updateReminderHistory(
        uid: String,
        history: ReminderHistory,
        status: String
    ): DataResult<Response<Any>> {
        return getResult {
            iReminderHistoryDatasource.updateReminderHistory(
                uid,
                history,
                status
            )
        }
    }

    override suspend fun getReminderHistoryDetail(uid: String, historyID: String): DataResult<Response<Any>> {
        return getResult {
            iReminderHistoryDatasource.getReminderHistoryDetail(uid, historyID)
        }
    }

}