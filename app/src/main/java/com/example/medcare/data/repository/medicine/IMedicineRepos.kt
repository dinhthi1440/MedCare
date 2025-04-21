package com.example.medcare.data.repository.medicine

import com.example.medcare.base.DataResult
import com.example.medcare.models.Medicine

interface IMedicineRepos {
    interface Local {
        suspend fun insertMedicine(medicine: Medicine): DataResult<Long>
        suspend fun getAllMedicine(): DataResult<List<Medicine>>
        suspend fun deleteMedicine(idMedicine: String): DataResult<Int>
    }
}