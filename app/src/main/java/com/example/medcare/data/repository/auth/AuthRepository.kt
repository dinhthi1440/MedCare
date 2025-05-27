package com.example.medcare.data.repository.auth

import com.example.medcare.base.BaseRepository
import com.example.medcare.base.DataResult
import com.example.medcare.data.datasource.auth.IAuthDatasource
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.models.Account
import com.example.medcare.models.Response

class AuthRepository(private val remote: IAuthDatasource) : BaseRepository(), IAuthRepository {
    override suspend fun registerAccount(email: String, password: String): DataResult<Response<Any>> {
        return getResult { remote.registerAccount(email, password) }
    }
    override suspend fun loginWithEmailPassword(email: String, password: String): Response<Any> {
        return remote.loginWithEmailPassword(email, password)
    }

    override suspend fun getUserData(uid: String): Response<Any> {
        return  remote.getUserData(uid)
    }

    override suspend fun createUser(account: Account): DataResult<Response<Any>> {
        return getResult { remote.createUser(account) }
    }

    override suspend fun updateUser(account: Account): Response<Any> {
        return remote.updateUser(account)
    }

    override suspend fun updateUserByFiled(
        accountID: String,
        fields: Map<String, Any>
    ): Response<Any> {
        return remote.updateUserByFiled(accountID, fields)
    }

    override suspend fun getAllUser(): DataResult<Response<Any>> {
        return getResult { remote.getAllUser() }
    }

    override suspend fun deleteUserByID(accountID: String): Response<Any> {
        return remote.deleteUserByID(accountID)
    }

    override suspend fun searchUserByName(searchString: String): DataResult<Response<Any>> {
        return getResult { remote.searchUserByName(searchString) }
    }
}