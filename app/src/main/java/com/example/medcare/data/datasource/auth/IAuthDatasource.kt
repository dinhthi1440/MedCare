package com.example.medcare.data.datasource.auth

import com.example.medcare.models.Account
import com.example.medcare.models.Response

interface IAuthDatasource {
    suspend fun registerAccount(email: String, password: String): Response<Any>
    suspend fun loginWithEmailPassword(email: String, password: String): Response<Any>
    suspend fun getUserData(uid: String): Response<Any>
    suspend fun createUser(account: Account): Response<Any>
}