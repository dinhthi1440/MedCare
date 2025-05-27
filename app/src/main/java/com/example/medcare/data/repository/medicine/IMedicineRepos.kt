package com.example.medcare.data.repository.medicine

import com.example.medcare.base.DataResult
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

interface IMedicineRepos {
    suspend fun insertMedicine(medicine: Medicine): DataResult<Long>
    suspend fun getAllMedicine(): DataResult<List<Medicine>>
    suspend fun deleteMedicine(idMedicine: String): DataResult<Int>
    suspend fun getMedicineById(idMedicine: String): DataResult<Medicine>
    suspend fun updateMedicine(medicine: Medicine): DataResult<Int>
    suspend fun insertMedicineRemote(uid: String, medicine: Medicine): Response<Any>
    suspend fun getAllMedicineRemote(uid: String): DataResult<Response<Any>>
    suspend fun getMedicineByIdRemote(uid: String, medicineID: String): DataResult<Response<Any>>
    suspend fun updateMedicineRemote(uid: String, medicine: Medicine): DataResult<Response<Any>>
    suspend fun deleteMedicineRemote(uid: String, medicineID: String): DataResult<Response<Any>>
}