package com.example.medcare.data.repository.auth

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.auth.IAuthDatasource
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Account
import com.example.medcare.models.Response

class AuthRepository(private val remote: IAuthDatasource.Remote) : BaseRepository(), IAuthRepository.Remote {
    override suspend fun registerAccount(email: String, password: String): DataResult<Response<Any>> {
        return getResult { remote.registerAccount(email, password) }
    }
    override suspend fun loginWithEmailPassword(email: String, password: String): DataResult<Response<Any>> {
        return getResult { remote.loginWithEmailPassword(email, password) }
    }

    override suspend fun getUserData(uid: String): DataResult<Response<Any>> {
        return getResult { remote.getUserData(uid) }
    }

    override suspend fun createUser(account: Account): DataResult<Response<Any>> {
        return getResult { remote.createUser(account) }
    }
}