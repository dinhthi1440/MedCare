package com.example.medcare.data.datasource.medicine

import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.Medicine

class MedicineDataSource(private val dataBaseLocal: DataBaseLocal): IMedicineDataSource.Local {
    override suspend fun insertMedicine(medicine: Medicine): Long {
        return dataBaseLocal.medicineDao.insertMedicine(medicine)
    }

    override suspend fun getAllMedicine(): List<Medicine> {
        return dataBaseLocal.medicineDao.getAllMedicine()
    }

    override suspend fun deleteMedicine(idMedicine: String): Int {
        return dataBaseLocal.medicineDao.deleteMedicine(idMedicine)
    }
}