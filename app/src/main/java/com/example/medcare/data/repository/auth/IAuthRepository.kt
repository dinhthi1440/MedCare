package com.example.medcare.data.repository.auth

import com.example.medcare.base.DataResult
import com.example.medcare.models.Response

interface IAuthRepository {
    interface Remote {
        suspend fun registerAccount(email: String, password: String): DataResult<Response<Any>>
        suspend fun loginWithEmailPassword(email: String, password: String): DataResult<Response<Any>>
        suspend fun getUserData(uid: String): DataResult<Response<Any>>
    }
}