package com.example.medcare.data.repository.medicine

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response

class MedicineRepos(
    private val dataSource: IMedicineDataSource
) : BaseRepository(), IMedicineRepos{
    override suspend fun insertMedicine(medicine: Medicine): DataResult<Long> {
        return getResult { dataSource.insertMedicine(medicine) }
    }

    override suspend fun getAllMedicine(): DataResult<List<Medicine>> {
        return getResult { dataSource.getAllMedicine() }
    }

    override suspend fun deleteMedicine(idMedicine: String): DataResult<Int> {
        return getResult { dataSource.deleteMedicine(idMedicine) }
    }

    override suspend fun getMedicineById(idMedicine: String): DataResult<Medicine> {
        return getResult { dataSource.getMedicineById(idMedicine) }
    }

    override suspend fun updateMedicine(medicine: Medicine): DataResult<Int> {
        return getResult { dataSource.updateMedicine(medicine) }
    }

    override suspend fun insertMedicineRemote(
        uid: String,
        medicine: Medicine
    ): DataResult<Response<Any>> {
        return getResult { dataSource.insertMedicineRemote(uid, medicine) }
    }

    override suspend fun getAllMedicineRemote(uid: String): DataResult<Response<Any>> {
        return getResult { dataSource.getAllMedicineRemote(uid) }
    }

    override suspend fun getMedicineByIdRemote(
        uid: String,
        medicineID: String
    ): DataResult<Response<Any>> {
        return getResult { dataSource.getMedicineByIdRemote(uid, medicineID) }
    }

    override suspend fun updateMedicineRemote(
        uid: String,
        medicine: Medicine
    ): DataResult<Response<Any>> {
        return getResult { dataSource.updateMedicineRemote(uid, medicine) }
    }

    override suspend fun deleteMedicineRemote(
        uid: String,
        medicineID: String
    ): DataResult<Response<Any>> {
        return getResult { dataSource.deleteMedicineRemote(uid, medicineID) }
    }

}