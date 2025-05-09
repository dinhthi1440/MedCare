package com.example.medcare.data.repository.pillreminder

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.pillreminder.IPillReminderDataSource
import com.example.medcare.views.pill_reminder.model.PillReminder

class PillReminderRepos(private val local: IPillReminderDataSource.Local): BaseRepository(), IPillReminderRepos.Local  {
    override suspend fun insertPillReminder(pillReminder: PillReminder): DataResult<Long> {
        return getResult { local.insertPillReminder(pillReminder) }
    }

    override suspend fun getAllPillReminder(): DataResult<List<PillReminder>> {
        return getResult { local.getAllPillReminder() }
    }

    override suspend fun deletePillReminder(idPillReminder: String): DataResult<Int> {
        return getResult { local.deletePillReminder(idPillReminder) }
    }

    override suspend fun updatePillReminder(pillReminder: PillReminder): DataResult<Int> {
        return getResult { local.updatePillReminder(pillReminder) }
    }

    override suspend fun getPillReminderById(reminderId: String): DataResult<PillReminder?> {
        return getResult { local.getPillReminderById(reminderId) }
    }
}