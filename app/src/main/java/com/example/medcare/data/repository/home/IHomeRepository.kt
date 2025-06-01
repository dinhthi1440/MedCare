package com.example.medcare.data.repository.home

import com.example.medcare.base.DataResult
import com.example.medcare.models.Medicine
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response

interface IHomeRepository {
    suspend fun insertAllMedicine(medicine: List<Medicine>): List<Long>

    suspend fun deleteAllMedicine(): Int
    suspend fun deleteAllReminder(): Int

    suspend fun insertAllReminder(pillReminder: List<PillReminder>): List<Long>
    suspend fun getAllReminderRemote(uid: String): Response<Any>
    suspend fun getAllMedicineRemote(uid: String): Response<Any>

    suspend fun getAllHistory(): List<ReminderHistory>
    suspend fun deleteAllHistory(): Int
    suspend fun insertHistoryRemote(uid: String, reminderHistories: List<ReminderHistory>): Response<Any>

    suspend fun getAllMedicine(): List<Medicine>
    suspend fun getAllPillReminder(): List<PillReminder>
}