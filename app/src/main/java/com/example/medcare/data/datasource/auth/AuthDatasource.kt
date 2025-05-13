package com.example.medcare.data.datasource.auth

import com.example.medcare.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resumeWithException


class AuthDatasource : IAuthDatasource.Remote {
    private val auth = FirebaseAuth.getInstance()

    override suspend fun registerAccount(email: String, password: String): Response<Any> {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response(200, "Đăng ký thành công", null))
                    } else {
                        val error = task.exception?.localizedMessage ?: "Đăng ký thất bại"
                        continuation.resume(Response(400, error, null))
                    }
                }
        }
    }

    override suspend fun loginWithEmailPassword(email: String, password: String): Response<Any> {
        return suspendCoroutine { continuation ->
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userID = task.result?.user?.uid
                        continuation.resume(
                            Response(200, "Đăng nhập thành công", userID)
                        )
                    } else {
                        val errorMessage = when (val exception = task.exception) {
                            is FirebaseAuthUserCollisionException -> "Email đã được sử dụng"
                            is FirebaseAuthWeakPasswordException -> "Mật khẩu yếu"
                            else -> exception?.localizedMessage ?: "Đăng ký thất bại"
                        }
                        continuation.resume(
                            Response(401, errorMessage, null)
                        )
                    }
                }
        }
    }
}