package com.example.medcare.data.datasource.auth

import com.example.medcare.models.Response

interface IAuthDatasource {
    interface Remote {
        suspend fun registerAccount(email: String, password: String): Response<Any>
        suspend fun loginWithEmailPassword(email: String, password: String): Response<Any>
        suspend fun getUserData(uid: String): Response<Any>
    }
}