package com.example.medcare.data.repository.home

import com.example.medcare.base.BaseRepository
import com.example.medcare.data.datasource.home.IHomeDatasource
import com.example.medcare.models.Medicine
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response

class HomeRepository(private val iHomeDatasource: IHomeDatasource) : BaseRepository(), IHomeRepository {
    override suspend fun insertAllMedicine(medicine: List<Medicine>): List<Long> {
        return iHomeDatasource.insertAllMedicine(medicine)
    }

    override suspend fun deleteAllMedicine(): Int {
        return iHomeDatasource.deleteAllMedicine()
    }

    override suspend fun deleteAllReminder(): Int {
        return iHomeDatasource.deleteAllReminder()
    }

    override suspend fun insertAllReminder(pillReminder: List<PillReminder>): List<Long> {
        return iHomeDatasource.insertAllReminder(pillReminder)
    }

    override suspend fun getAllReminderRemote(uid: String): Response<Any> {
        return iHomeDatasource.getAllReminderRemote(uid)
    }

    override suspend fun getAllMedicineRemote(uid: String): Response<Any> {
        return iHomeDatasource.getAllMedicineRemote(uid)
    }

    override suspend fun getAllHistory(): List<ReminderHistory> {
        return iHomeDatasource.getAllHistory()
    }

    override suspend fun deleteAllHistory(): Int {
        return iHomeDatasource.deleteAllHistory()
    }

    override suspend fun insertHistoryRemote(
        uid: String,
        reminderHistories: List<ReminderHistory>
    ): Response<Any> {
        return iHomeDatasource.insertHistoryRemote(uid, reminderHistories)
    }

    override suspend fun getAllMedicine(): List<Medicine> {
        return iHomeDatasource.getAllMedicine()
    }

    override suspend fun getAllPillReminder(): List<PillReminder> {
        return iHomeDatasource.getAllPillReminder()
    }
}