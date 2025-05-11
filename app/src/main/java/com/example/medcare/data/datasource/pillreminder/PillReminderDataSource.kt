package com.example.medcare.data.datasource.pillreminder

import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.PillReminder

class PillReminderDataSource(private val dataBaseLocal: DataBaseLocal): IPillReminderDataSource.Local  {
    override suspend fun insertPillReminder(pillReminder: PillReminder): Long {
        return dataBaseLocal.pillReminderDAO.insertPillReminder(pillReminder)
    }

    override suspend fun getAllPillReminder(): List<PillReminder> {
        return dataBaseLocal.pillReminderDAO.getAllPillReminder()
    }

    override suspend fun deletePillReminder(idPillReminder: String): Int {
        return dataBaseLocal.pillReminderDAO.deletePillReminder(idPillReminder)
    }

    override suspend fun updatePillReminder(pillReminder: PillReminder): Int {
        return dataBaseLocal.pillReminderDAO.updatePillReminder(pillReminder)
    }

    override suspend fun getPillReminderById(reminderId: String): PillReminder? {
        return dataBaseLocal.pillReminderDAO.getPillReminderById(reminderId)
    }
}