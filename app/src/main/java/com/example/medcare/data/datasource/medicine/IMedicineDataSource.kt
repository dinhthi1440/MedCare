package com.example.medcare.data.datasource.medicine

import androidx.room.Query
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.Medicine

interface IMedicineDataSource {
    interface Local {
        suspend fun insertMedicine(medicine: Medicine): Long
        suspend fun getAllMedicine(): List<Medicine>
        suspend fun deleteMedicine(idMedicine: String): Int
    }
}