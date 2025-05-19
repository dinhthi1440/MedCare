package com.example.medcare.data.datasource.medicine

import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

interface IMedicineDataSource {
    suspend fun insertMedicine(medicine: Medicine): Long
    suspend fun getAllMedicine(): List<Medicine>
    suspend fun deleteMedicine(idMedicine: String): Int
    suspend fun getMedicineById(idMedicine: String): Medicine
    suspend fun updateMedicine(medicine: Medicine): Int
    suspend fun insertMedicineRemote(uid: String, medicine: Medicine): Response<Any>
    suspend fun getAllMedicineRemote(uid: String): Response<Any>
    suspend fun getMedicineByIdRemote(uid: String, medicineID: String): Response<Any>
    suspend fun updateMedicineRemote(uid: String, medicine: Medicine): Response<Any>
    suspend fun deleteMedicineRemote(uid: String, medicineID: String): Response<Any>

}