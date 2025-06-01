package com.example.medcare.data.repository.pillreminder

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.pillreminder.IPillReminderDataSource
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response

class PillReminderRepos(private val dataSource: IPillReminderDataSource): BaseRepository(), IPillReminderRepos  {
    override suspend fun insertPillReminder(pillReminder: PillReminder): Long {
        return dataSource.insertPillReminder(pillReminder)
    }

    override suspend fun getAllPillReminder(): DataResult<List<PillReminder>> {
        return getResult { dataSource.getAllPillReminder() }
    }

    override suspend fun deletePillReminder(idPillReminder: String): DataResult<Int> {
        return getResult { dataSource.deletePillReminder(idPillReminder) }
    }

    override suspend fun updatePillReminder(pillReminder: PillReminder): DataResult<Int> {
        return getResult { dataSource.updatePillReminder(pillReminder) }
    }

    override suspend fun getPillReminderById(reminderId: String): DataResult<PillReminder?> {
        return getResult { dataSource.getPillReminderById(reminderId) }
    }

    override suspend fun insertPillReminderRemote(
        uid: String,
        pillReminder: PillReminder
    ): DataResult<Response<Any>> {
        return getResult { dataSource.insertPillReminderRemote(uid, pillReminder) }
    }

    override suspend fun getAllPillReminderRemote(uid: String): DataResult<Response<Any>> {
        return getResult { dataSource.getAllPillReminderRemote(uid) }
    }

    override suspend fun deletePillReminderRemote(
        uid: String,
        idPillReminder: String
    ): Response<Any> {
        return dataSource.deletePillReminderRemote(uid, idPillReminder)
    }

    override suspend fun updatePillReminderRemote(
        uid: String,
        pillReminder: PillReminder
    ): Response<Any> {
        return dataSource.updatePillReminderRemote(uid, pillReminder)
    }

    override suspend fun updateReminderFieldsRemote(
        uid: String,
        reminderID: String,
        reminderFields: HashMap<String, Any>
    ): DataResult<Response<Any>> {
        return getResult { dataSource.updateReminderFieldsRemote(uid, reminderID, reminderFields) }
    }

    override suspend fun getPillReminderByIdRemote(
        uid: String,
        reminderId: String
    ): DataResult<Response<Any>> {
        return getResult { dataSource.getPillReminderByIdRemote(uid, reminderId) }
    }

    override suspend fun insertReminderRelativeRemote(
        reminderRelative: PillReminder
    ): Response<Any> {
        return dataSource.insertReminderRelativeRemote(reminderRelative)
    }

    override suspend fun insertReminderHistory(
        uid: String,
        reminderHistory: ReminderHistory
    ): DataResult<Response<Any>> {
        return getResult { dataSource.insertReminderHistory(uid, reminderHistory) }
    }
}