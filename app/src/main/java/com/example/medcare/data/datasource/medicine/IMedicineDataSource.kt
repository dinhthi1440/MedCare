package com.example.medcare.data.datasource.medicine

import com.example.medcare.models.Medicine
import com.example.medcare.views.pill_reminder.model.PillReminder

interface IMedicineDataSource {
    interface Local {
        suspend fun insertMedicine(medicine: Medicine): Long
        suspend fun getAllMedicine(): List<Medicine>
        suspend fun deleteMedicine(idMedicine: String): Int
        suspend fun getMedicineById(idMedicine: String): Medicine
        suspend fun updateMedicine(medicine: Medicine): Int
    }
}