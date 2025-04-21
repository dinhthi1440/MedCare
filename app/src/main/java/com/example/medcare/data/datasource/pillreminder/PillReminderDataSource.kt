package com.example.medcare.data.datasource.pillreminder

import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.views.medication_reminder.model.PillReminder

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
}