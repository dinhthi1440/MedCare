package com.example.medcare.data.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.ReminderHistory

@Dao
interface HistoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHistory(history: ReminderHistory): Long

    @Query("SELECT * FROM ${DataBaseLocal.TABLE_HISTORY_REMINDER}")
    fun getAllMedicine(): List<ReminderHistory>

    @Query("DELETE FROM ${DataBaseLocal.TABLE_HISTORY_REMINDER}")
    fun deleteAllHistory(): Int
}
