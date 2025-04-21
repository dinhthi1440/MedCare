package com.example.medcare.data.datasource.pillreminder

import androidx.room.Query
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.views.medication_reminder.model.PillReminder

interface IPillReminderDataSource {
    interface Local {
        suspend fun insertPillReminder(pillReminder: PillReminder): Long
        suspend fun getAllPillReminder(): List<PillReminder>
        suspend fun deletePillReminder(idPillReminder: String): Int
    }
}