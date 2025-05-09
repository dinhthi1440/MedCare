package com.example.medcare.data.datasource.pillreminder

import com.example.medcare.views.pill_reminder.model.PillReminder

interface IPillReminderDataSource {
    interface Local {
        suspend fun insertPillReminder(pillReminder: PillReminder): Long
        suspend fun getAllPillReminder(): List<PillReminder>
        suspend fun deletePillReminder(idPillReminder: String): Int
        suspend fun updatePillReminder(pillReminder: PillReminder): Int
        suspend fun getPillReminderById(reminderId: String): PillReminder?
    }
}