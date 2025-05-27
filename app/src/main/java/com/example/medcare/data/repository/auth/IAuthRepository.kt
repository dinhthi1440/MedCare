package com.example.medcare.data.repository.auth

import com.example.medcare.base.DataResult
import com.example.medcare.models.Account
import com.example.medcare.models.Response

interface IAuthRepository {
    suspend fun registerAccount(email: String, password: String): DataResult<Response<Any>>
    suspend fun loginWithEmailPassword(email: String, password: String): Response<Any>
    suspend fun getUserData(uid: String): Response<Any>
    suspend fun createUser(account: Account): DataResult<Response<Any>>
    suspend fun updateUser(account: Account): Response<Any>
    suspend fun updateUserByFiled(accountID: String, fields: Map<String, Any>): Response<Any>
    suspend fun getAllUser(): DataResult<Response<Any>>
    suspend fun deleteUserByID(accountID: String): Response<Any>
    suspend fun searchUserByName(searchString: String): DataResult<Response<Any>>
}