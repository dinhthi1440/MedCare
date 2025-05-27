package com.example.medcare.data.datasource.feedback

import com.example.medcare.models.Feedback
import com.example.medcare.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FeedbackDatasource : IFeedbackDatasource {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun insertFeedback(feedback: Feedback): Response<Any> {
        return try {
            db.collection("feed_backs").add(feedback).await()
            Response(200, "Gửi phản hồi thành công")
        } catch (e: Exception) {
            Response(500, "Lỗi khi gửi phản hồi: ${e.message}")
        }
    }

    override suspend fun updateUserByFiled(accountID: String, fields: Map<String, Any>): Response<Any> {
        return try {
            db.collection("users").document(accountID).update(fields).await()
            Response(200, "Cập nhật người dùng thành công")
        } catch (e: Exception) {
            Response(500, "Lỗi khi cập nhật: ${e.message}")
        }
    }

    override suspend fun getAllFeedback(): Response<Any> {
        return suspendCoroutine { continuation ->
            db.collection("feed_backs")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val feedbackList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Feedback::class.java)?.apply { id = document.id }
                    }
                    if (feedbackList.isEmpty()) {
                        continuation.resume(Response(204, "Danh sách phản hồi trống", emptyList<Feedback>()))
                    } else {
                        continuation.resume(Response(200, "Lấy danh sách phản hồi thành công", feedbackList))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(Response(500, "Lỗi: ${e.message}", null))
                }
        }
    }

    override suspend fun getFeedbackByID(feedbackID: String): Response<Any> {
        return suspendCoroutine { continuation ->
            db.collection("feed_backs")
                .document(feedbackID)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val feedback = document.toObject(Feedback::class.java)?.apply { id = document.id }
                        continuation.resume(Response(200, "Lấy phản hồi thành công", feedback))
                    } else {
                        continuation.resume(Response(404, "Không tìm thấy phản hồi", null))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(Response(500, "Lỗi khi truy xuất phản hồi: ${e.message}", null))
                }
        }
    }

    override suspend fun deleteFeedbackByID(feedbackID: String): Response<Any> {
        return try {
            db.collection("feed_backs").document(feedbackID).delete().await()
            Response(200, "Xóa phản hồi thành công")
        } catch (e: Exception) {
            Response(500, "Lỗi khi xóa phản hồi: ${e.message}")
        }
    }

    override suspend fun searchFeedback(searchString: String): Response<Any> {
        return suspendCoroutine { continuation ->
            db.collection("feed_backs")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val filteredList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Feedback::class.java)?.apply { id = document.id }
                    }.filter { feedback ->
                        feedback.content?.contains(searchString, ignoreCase = true) == true ||
                                feedback.senderName?.contains(searchString, ignoreCase = true) == true
                    }

                    if (filteredList.isEmpty()) {
                        continuation.resume(Response(204, "Không tìm thấy phản hồi nào phù hợp", emptyList<Feedback>()))
                    } else {
                        continuation.resume(Response(200, "Tìm kiếm thành công", filteredList))
                    }
                }
                .addOnFailureListener { e ->
                    continuation.resume(Response(500, "Lỗi: ${e.message}", null))
                }
        }
    }
}
