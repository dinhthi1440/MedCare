package com.example.medcare.data.repository.medicine

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.models.Medicine

class MedicineRepos(private val local: IMedicineDataSource.Local): BaseRepository(), IMedicineRepos.Local {
    override suspend fun insertMedicine(medicine: Medicine): DataResult<Long> {
        return getResult { local.insertMedicine(medicine) }
    }

    override suspend fun getAllMedicine(): DataResult<List<Medicine>> {
        return getResult { local.getAllMedicine() }
    }

    override suspend fun deleteMedicine(idMedicine: String): DataResult<Int> {
        return getResult { local.deleteMedicine(idMedicine) }
    }

    override suspend fun getMedicineById(idMedicine: String): DataResult<Medicine> {
        return getResult { local.getMedicineById(idMedicine) }
    }

    override suspend fun updateMedicine(medicine: Medicine): DataResult<Int> {
        return getResult { local.updateMedicine(medicine) }
    }

}