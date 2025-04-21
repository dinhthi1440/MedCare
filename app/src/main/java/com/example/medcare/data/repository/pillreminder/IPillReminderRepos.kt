package com.example.medcare.data.repository.pillreminder

import com.example.medcare.base.DataResult
import com.example.medcare.views.medication_reminder.model.PillReminder

interface IPillReminderRepos {
    interface Local {
        suspend fun insertPillReminder(pillReminder: PillReminder): DataResult<Long>
        suspend fun getAllPillReminder(): DataResult<List<PillReminder>>
        suspend fun deletePillReminder(idPillReminder: String): DataResult<Int>
    }
}