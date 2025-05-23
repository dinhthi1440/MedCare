package com.example.medcare.data.datasource.pillreminder

import android.util.Log
import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderRelative
import com.example.medcare.models.Response
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class PillReminderDataSource(private val dataBaseLocal: DataBaseLocal): IPillReminderDataSource  {
    override suspend fun insertPillReminder(pillReminder: PillReminder): Long {
        return dataBaseLocal.pillReminderDAO.insertPillReminder(pillReminder)
    }

    override suspend fun getAllPillReminder(): List<PillReminder> {
        return dataBaseLocal.pillReminderDAO.getAllPillReminder()
    }

    override suspend fun deletePillReminder(idPillReminder: String): Int {
        return dataBaseLocal.pillReminderDAO.deletePillReminder(idPillReminder)
    }

    override suspend fun updatePillReminder(pillReminder: PillReminder): Int {
        return dataBaseLocal.pillReminderDAO.updatePillReminder(pillReminder)
    }

    override suspend fun getPillReminderById(reminderId: String): PillReminder? {
        return dataBaseLocal.pillReminderDAO.getPillReminderById(reminderId)
    }

    override suspend fun insertPillReminderRemote(
        uid: String,
        pillReminder: PillReminder
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid)
                .collection("pill_reminders")
                .document(pillReminder.id)
                .set(pillReminder)
                .addOnSuccessListener {
                    continuation.resume(Response(200, "Thêm nhắc thuốc thành công", true))
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Thêm nhắc thuốc thất bại", false))
                }
        }
    }


    override suspend fun getAllPillReminderRemote(uid: String): Response<Any> {
        return suspendCoroutine { continuation ->
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .document(uid)
                    .collection("pill_reminders")
                    .get()
                    .addOnSuccessListener { result ->
                        try {
                            val list = result.documents.mapNotNull { doc ->
                                doc.toObject(PillReminder::class.java)?.apply { id = doc.id }
                            }

                            if (list.isNotEmpty()) {
                                continuation.resume(Response(200, "Lấy danh sách nhắc thuốc thành công", list))
                            } else {
                                continuation.resume(Response(204, "Không có lời nhắc thuốc nào", emptyList<PillReminder>()))
                            }
                        } catch (e: Exception) {
                            continuation.resume(Response(500, "Lỗi xử lý dữ liệu nhắc thuốc", null))
                        }
                    }
                    .addOnFailureListener { _ ->
                        continuation.resume(Response(500, "Lỗi khi lấy danh sách nhắc thuốc", null))
                    }
            } catch (e: Exception) {
                continuation.resume(Response(500, "Lỗi khi kết nối tới Firestore", null))
            }
        }
    }


    override suspend fun deletePillReminderRemote(
        uid: String,
        idPillReminder: String
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("users")
                .document(uid)
                .collection("pill_reminders")
                .document(idPillReminder)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.delete()
                            .addOnSuccessListener {
                                continuation.resume(Response(200, "Xoá nhắc thuốc thành công", true))
                            }
                            .addOnFailureListener {
                                continuation.resume(Response(500, "Lỗi khi xoá", false))
                            }
                    } else {
                        continuation.resume(Response(404, "Không tìm thấy nhắc thuốc để xoá", false))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi kiểm tra nhắc thuốc", false))
                }
        }
    }


    override suspend fun updatePillReminderRemote(
        uid: String,
        pillReminder: PillReminder
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("users")
                .document(uid)
                .collection("pill_reminders")
                .document(pillReminder.id)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.set(pillReminder)
                            .addOnSuccessListener {
                                continuation.resume(Response(200, "Cập nhật nhắc thuốc thành công", true))
                            }
                            .addOnFailureListener {
                                continuation.resume(Response(500, "Lỗi khi cập nhật", false))
                            }
                    } else {
                        continuation.resume(Response(404, "Không tìm thấy nhắc thuốc để cập nhật", false))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi kiểm tra nhắc thuốc", false))
                }
        }
    }

    override suspend fun updateReminderFieldsRemote(
        uid: String,
        reminderID: String,
        reminderFields: HashMap<String, Any>
    ): Response<Any> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users")
            .document(uid)
            .collection("pill_reminders")
            .document(reminderID)
        if (reminderFields.isEmpty()) {
            continuation.resume(
                Response(
                    400,
                    "Không có dữ liệu nào để cập nhật",
                    false
                )
            )
            return@suspendCoroutine
        }
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    docRef.update(reminderFields)
                        .addOnSuccessListener {
                            continuation.resume(
                                Response(
                                    200,
                                    "Cập nhật nhắc thuốc thành công",
                                    true
                                )
                            )
                        }
                        .addOnFailureListener { e ->
                            continuation.resume(
                                Response(
                                    500,
                                    "Lỗi khi cập nhật",
                                    false
                                )
                            )
                        }
                } else {
                    continuation.resume(
                        Response(
                            404,
                            "Không tìm thấy nhắc thuốc để cập nhật",
                            false
                        )
                    )
                }
            }
            .addOnFailureListener { _ ->
                continuation.resume(
                    Response(
                        500,
                        "Lỗi khi kiểm tra nhắc thuốc",
                        false
                    )
                )
            }
    }



    override suspend fun getPillReminderByIdRemote(uid: String, reminderId: String): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid)
                .collection("pill_reminders")
                .document(reminderId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val reminder = document.toObject(PillReminder::class.java)?.apply { id = document.id }
                        continuation.resume(Response(200, "Lấy nhắc thuốc thành công", reminder))
                    } else {
                        continuation.resume(Response(404, "Không tìm thấy nhắc thuốc", null))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi lấy nhắc thuốc", null))
                }
        }
    }

    override suspend fun insertReminderRelativeRemote(
        reminderRelative: ReminderRelative
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            val toRef = db.collection("users")
                .document(reminderRelative.receiverID)
                .collection("reminder_relative_from")
                .document(reminderRelative.pillReminder.id)

            val fromRef = db.collection("users")
                .document(reminderRelative.senderID)
                .collection("reminder_relative_to")
                .document(reminderRelative.pillReminder.id)
            val task1 = toRef.set(reminderRelative)
            val reminderCopy = reminderRelative
            val task2 = fromRef.set(reminderCopy)

            Tasks.whenAllComplete(task1, task2)
                .addOnSuccessListener { tasks ->
                    val failedTasks = tasks.filter { !it.isSuccessful }
                    if (failedTasks.isEmpty()) {
                        continuation.resume(Response(200, "Thêm nhắc thuốc thành công", true))
                    } else {
                        val errorMsg = failedTasks.joinToString("\n") { it.exception?.message ?: "Lỗi không xác định" }
                        continuation.resume(Response(500, "Một hoặc nhiều thao tác thất bại: $errorMsg", false))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi thêm nhắc thuốc: ${it.message}", false))
                }
        }
    }



}