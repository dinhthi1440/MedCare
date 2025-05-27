package com.example.medcare.data.datasource.reminder_history

import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ReminderHistoryDatasource : IReminderHistoryDatasource {
    private val db = FirebaseFirestore.getInstance()
    private val collectionInUser = "reminder_history"
    override suspend fun insertReminderHistory(
        uid: String,
        reminderHistory: ReminderHistory
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            db.collection("users")
                .document(uid).collection(collectionInUser).document(reminderHistory.id)
                .set(reminderHistory)
                .addOnSuccessListener {
                    continuation.resume(Response(200, "Thêm lịch sử dùng thuốc thành công", true))
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi thêm lịch sử dùng thuốc", false))
                }
        }
    }

    override suspend fun getAllReminderHistory(uid: String): Response<Any> {
        return suspendCoroutine { continuation ->
            try {
                db.collection("users")
                    .document(uid)
                    .collection(collectionInUser)
                    .get()
                    .addOnSuccessListener { result ->
                        try {
                            val list = result.documents.mapNotNull { doc ->
                                doc.toObject(ReminderHistory::class.java)?.apply { id = doc.id }
                            }

                            if (list.isNotEmpty()) {
                                continuation.resume(
                                    Response(
                                        200,
                                        "Lấy danh sách lịch sử dùng thuốc thành công",
                                        list
                                    )
                                )
                            } else {
                                continuation.resume(
                                    Response(
                                        204,
                                        "Không có lịch sử dùng thuốc nào",
                                        emptyList<ReminderHistory>()
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            continuation.resume(
                                Response(
                                    500,
                                    "Lỗi xử lý dữ liệu lịch sử dùng thuốc",
                                    null
                                )
                            )
                        }
                    }
                    .addOnFailureListener { _ ->
                        continuation.resume(
                            Response(
                                500,
                                "Lỗi khi lấy danh sách lịch sử dùng thuốc",
                                null
                            )
                        )
                    }
            } catch (e: Exception) {
                continuation.resume(Response(500, "Lỗi khi kết nối tới Firestore", null))
            }
        }
    }

    override suspend fun updateReminderHistory(
        uid: String,
        history: ReminderHistory,
        status: String
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val docRef = db.collection("users")
                .document(uid)
                .collection(collectionInUser)
                .document(history.id)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.update("status", status)
                            .addOnSuccessListener {
                                continuation.resume(
                                    Response(
                                        200,
                                        "Xác nhận trạng thái mới thành công",
                                        true
                                    )
                                )
                            }
                            .addOnFailureListener {
                                continuation.resume(
                                    Response(
                                        500,
                                        "Lỗi khi cập nhật trạng thái mới",
                                        false
                                    )
                                )
                            }
                    } else {
                        continuation.resume(
                            Response(
                                404,
                                "Không tìm thấy lịch sử dùng thuốc",
                                false
                            )
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi kiểm tra lịch sử dùng thuốc", false))
                }
        }
    }

    override suspend fun getReminderHistoryDetail(uid: String, historyID: String): Response<Any> {
        return suspendCoroutine { continuation ->
            try {
                val docRef = db.collection("users")
                    .document(uid)
                    .collection(collectionInUser)
                    .document(historyID)

                docRef.get()
                    .addOnSuccessListener { result ->
                        if (result.exists()) {
                            try {
                                val reminder = result.toObject(ReminderHistory::class.java)?.apply { id = result.id }
                                continuation.resume(
                                    Response(
                                        200,
                                        "Lấy chi tiết lịch sử dùng thuốc thành công",
                                        reminder
                                    )
                                )
                            } catch (e: Exception) {
                                continuation.resume(
                                    Response(
                                        500,
                                        "Lỗi xử lý dữ liệu lịch sử dùng thuốc",
                                        null
                                    )
                                )
                            }
                        } else {
                            continuation.resume(
                                Response(
                                    404,
                                    "Lịch sử dùng thuốc này đã bị xóa",
                                    null
                                )
                            )
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(
                            Response(
                                500,
                                "Lỗi khi lấy dữ liệu, hãy thử lại",
                                null
                            )
                        )
                    }
            } catch (e: Exception) {
                continuation.resume(
                    Response(
                        500,
                        "Lỗi khi kết nối tới Firestore",
                        null
                    )
                )
            }
        }
    }


}