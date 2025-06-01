package com.example.medcare.data.database.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.Medicine

@Dao
interface MedicineDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicine(medicine: Medicine): Long

    @Query("SELECT * FROM ${DataBaseLocal.TABLE_MEDICINE}")
    fun getAllMedicine(): List<Medicine>

    @Query("DELETE FROM ${DataBaseLocal.TABLE_MEDICINE} WHERE id = :idMedicine")
    fun deleteMedicine(idMedicine: String): Int

    @Query("SELECT * FROM ${DataBaseLocal.TABLE_MEDICINE} WHERE id = :idMedicine LIMIT 1")
    fun getMedicineById(idMedicine: String): Medicine

    @Update
    fun updateMedicine(medicine: Medicine): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllMedicine(medicine: List<Medicine>): List<Long>

    @Query("DELETE FROM ${DataBaseLocal.TABLE_MEDICINE}")
    fun deleteAllMedicine(): Int
}