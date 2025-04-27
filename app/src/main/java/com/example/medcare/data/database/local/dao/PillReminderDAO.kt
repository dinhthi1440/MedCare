package com.example.medcare.data.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.Converters
import com.example.medcare.views.pill_reminder.model.PillReminder

@Dao
interface PillReminderDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPillReminder(pillReminder: PillReminder): Long

    @Query("SELECT * FROM ${DataBaseLocal.TABLE_PILL_REMINDER}")
    fun getAllPillReminder(): List<PillReminder>
    @Query("SELECT * FROM ${DataBaseLocal.TABLE_PILL_REMINDER} WHERE id =:reminderId")
    fun getPillReminderById(reminderId: String): PillReminder?

    @Query("DELETE FROM ${DataBaseLocal.TABLE_PILL_REMINDER} WHERE id =:idPillReminder")
    fun deletePillReminder(idPillReminder: String): Int

    @Update
    fun updatePillReminder(pillReminder: PillReminder): Int
}