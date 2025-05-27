package com.example.medcare.data.datasource.auth

import com.example.medcare.models.Account
import com.example.medcare.models.Response

interface IAuthDatasource {
    suspend fun registerAccount(email: String, password: String): Response<Any>
    suspend fun loginWithEmailPassword(email: String, password: String): Response<Any>
    suspend fun getUserData(uid: String): Response<Any>
    suspend fun createUser(account: Account): Response<Any>
    suspend fun updateUser(account: Account): Response<Any>

    suspend fun updateUserByFiled(accountID: String, fields: Map<String, Any>): Response<Any>

    suspend fun getAllUser(): Response<Any>
    suspend fun deleteUserByID(accountID: String): Response<Any>
    suspend fun searchUserByName(searchString: String): Response<Any>
}