package com.example.medcare.data.datasource.auth

import com.example.medcare.models.Account
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class AuthDatasource : IAuthDatasource {
    private val auth = FirebaseAuth.getInstance()

    override suspend fun registerAccount(email: String, password: String): Response<Any> {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Response(201, "Đăng ký thành công", null))
                    } else {
                        val (statusCode, message) = when (val exception = task.exception) {
                            is FirebaseAuthUserCollisionException -> 409 to "Email đã được sử dụng"
                            is FirebaseAuthWeakPasswordException -> 400 to "Mật khẩu quá yếu"
                            is FirebaseAuthInvalidCredentialsException -> 400 to "Email không hợp lệ"
                            else -> 500 to (exception?.localizedMessage ?: "Đăng ký thất bại")
                        }
                        continuation.resume(Response(statusCode, message, null))
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
                        continuation.resume(Response(200, "Đăng nhập thành công", userID))
                    } else {
                        val (statusCode, message) = when (val exception = task.exception) {
                            is FirebaseAuthInvalidUserException -> 404 to "Email chưa được đăng ký"
                            is FirebaseAuthInvalidCredentialsException -> 401 to "Mật khẩu không đúng hoặc email sai"
                            else -> 500 to (exception?.localizedMessage ?: "Đăng nhập thất bại")
                        }
                        continuation.resume(Response(statusCode, message, null))
                    }
                }
        }
    }

    override suspend fun getUserData(uid: String): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val fullName = document.getString("fullName") ?: ""
                        val email = document.getString("email") ?: ""
                        val avatar = document.getString("avatar") ?: ""
                        val rule = document.getString("rule") ?: ""
                        val status = document.getString("status") ?: ""
                        val userData = Account(
                            uid, fullName, email, avatar, rule, status
                        )
                        continuation.resume(Response(200, "Lấy dữ liệu thành công", userData))
                    } else {
                        continuation.resume(Response(404, "Không tìm thấy người dùng", null))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Response(500, exception.localizedMessage ?: "Lỗi không xác định", null))
                }
        }
    }

    override suspend fun createUser(account: Account): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(account.id)
                .set(account)
                .addOnSuccessListener {
                    continuation.resume(Response(200, "Tạo dữ liệu người dùng thành công", true))
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Tạo dữ liệu thất bại: ${it.message}", false))
                }
        }
    }
    override suspend fun updateUser(account: Account): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            val updates = mapOf(
                "fullName" to account.fullName,
                "avatar" to account.avatar,
                "rule" to account.rule
            )

            db.collection("users")
                .document(account.id)
                .update(updates)
                .addOnSuccessListener {
                    continuation.resume(Response(200, "Cập nhật người dùng thành công", true))
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Cập nhật thất bại: ${it.message}", false))
                }
        }
    }

    override suspend fun updateUserByFiled(accountID: String, fields: Map<String, Any>): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(accountID)
                .update(fields)
                .addOnSuccessListener {
                    continuation.resume(Response(200, "Đã sửa thông tin", true))
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Cập nhật thất bại", false))
                }
        }
    }

    override suspend fun getAllUser(): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .whereNotEqualTo("rule", "admin")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val userList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Account::class.java)?.apply { id = document.id }
                    }
                    if (userList.isEmpty()) {
                        continuation.resume(Response(204, "Danh sách người dùng trống", emptyList<Account>()))
                    } else {
                        continuation.resume(Response(200, "Lấy danh sách người dùng thành công", userList))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(
                        Response(
                            500,
                            "Lỗi khi lấy dữ liệu, hãy thử lại",
                            null
                        )
                    )
                }
        }
    }

    override suspend fun deleteUserByID(accountID: String): Response<Any> {
        return try {
            val db = FirebaseFirestore.getInstance()
            val documentRef = db.collection("users").document(accountID)

            val snapshot = documentRef.get().await()

            if (!snapshot.exists()) {
                return Response(404, "Người dùng không tồn tại")
            }

            documentRef.delete().await()
            Response(200, "Xóa người dùng thành công")
        } catch (e: Exception) {
            Response(500, "Lỗi khi xóa người dùng")
        }
    }

    override suspend fun searchUserByName(searchString: String): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .whereNotEqualTo("rule", "admin")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val filteredList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Account::class.java)?.apply { id = document.id }
                    }.filter { account ->
                        account.fullName?.contains(searchString, ignoreCase = true) == true
                    }

                    if (filteredList.isEmpty()) {
                        continuation.resume(Response(204, "Không tìm thấy người dùng phù hợp", emptyList<Account>()))
                    } else {
                        continuation.resume(Response(200, "Tìm thấy người dùng phù hợp", filteredList))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(
                        Response(
                            500,
                            "Lỗi khi lấy dữ liệu, hãy thử lại",
                            null
                        )
                    )
                }
        }
    }



}