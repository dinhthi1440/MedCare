package com.example.medcare.data.datasource.relatives

import android.util.Log
import com.example.medcare.models.Account
import com.example.medcare.models.PillReminder
import com.example.medcare.models.PillReminderResult
import com.example.medcare.models.Relative
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.ReminderRequestStatus
import com.example.medcare.models.Response
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RelativesDataSource : IRelativesDataSource {
    val db = FirebaseFirestore.getInstance()
    override suspend fun insertRelativesRemote(uid: String, relative: Relative): Response<Any> {
        return suspendCoroutine { continuation ->
            db.collection("users")
                .document(uid).collection("relatives").document(relative.id)
                .set(relative)
                .addOnSuccessListener {
                    continuation.resume(Response(200, "Thêm người thân thành công", true))
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi thêm người thân", false))
                }
        }
    }

    override suspend fun updateRelativesRemote(uid: String, relative: Relative): Response<Any> {
        return suspendCoroutine { continuation ->
            val userToRelativeRef = db.collection("users")
                .document(uid)
                .collection("relatives")
                .document(relative.id)

            val relativeToUserRef = db.collection("users")
                .document(relative.id)
                .collection("relatives")
                .document(uid)

            userToRelativeRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // 1. Cập nhật relativeTitle trong document A
                        userToRelativeRef.update("relativeTitle", relative.relativeTitle)
                            .addOnSuccessListener {
                                // 2. Cập nhật 3 quyền trong document B
                                val updateMap = mapOf(
                                    "myCanInsertMedicine" to relative.canInsertMedicine,
                                    "myCanInsertReminder" to relative.canInsertReminder,
                                    "myCanSeeReminderHistory" to relative.canSeeReminderHistory
                                )
                                relativeToUserRef.update(updateMap)
                                    .addOnSuccessListener {
                                        continuation.resume(
                                            Response(
                                                200,
                                                "Cập nhật người thân thành công",
                                                true
                                            )
                                        )
                                    }
                                    .addOnFailureListener {
                                        continuation.resume(
                                            Response(
                                                500,
                                                "Lỗi khi cập nhật quyền người thân",
                                                false
                                            )
                                        )
                                    }
                            }
                            .addOnFailureListener {
                                continuation.resume(
                                    Response(
                                        500,
                                        "Lỗi khi cập nhật chức danh người thân",
                                        false
                                    )
                                )
                            }
                    } else {
                        continuation.resume(Response(404, "Người thân không tồn tại", false))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi kiểm tra người thân", false))
                }
        }
    }


    override suspend fun getAllRelativesRemote(uid: String): Response<Any> {
        return suspendCoroutine { continuation ->
            try {

                db.collection("users")
                    .document(uid)
                    .collection("relatives")
                    .get()
                    .addOnSuccessListener { result ->
                        try {
                            val list = result.documents.mapNotNull { doc ->
                                doc.toObject(Relative::class.java)?.apply { id = doc.id }
                            }

                            if (list.isNotEmpty()) {
                                continuation.resume(
                                    Response(
                                        200,
                                        "Lấy danh sách người thân thành công",
                                        list
                                    )
                                )
                            } else {
                                continuation.resume(
                                    Response(
                                        204,
                                        "Không có người thân nào",
                                        emptyList<Relative>()
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            continuation.resume(Response(500, "Lỗi xử lý dữ liệu người thân", null))
                        }
                    }
                    .addOnFailureListener { _ ->
                        continuation.resume(Response(500, "Lỗi khi lấy danh sách người thân", null))
                    }
            } catch (e: Exception) {
                continuation.resume(Response(500, "Lỗi khi kết nối tới Firestore", null))
            }
        }
    }

    override suspend fun deleteRelativesRemote(uid: String, idRelative: String): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("users")
                .document(uid)
                .collection("relatives")
                .document(idRelative)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.delete()
                            .addOnSuccessListener {
                                continuation.resume(
                                    Response(
                                        200,
                                        "Huỷ kết nối với người thân thành công",
                                        true
                                    )
                                )
                            }
                            .addOnFailureListener {
                                continuation.resume(Response(500, "Lỗi khi xoá", false))
                            }
                    } else {
                        continuation.resume(
                            Response(
                                404,
                                "Không tìm thấy người thân để xoá",
                                false
                            )
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi kiểm tra người thân", false))
                }
        }
    }

    override suspend fun getSearchRelativesRemote(searchString: String): Response<Any> =
        suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collectionGroup("users") // tìm trong tất cả collection con tên "relatives"
                .get()
                .addOnSuccessListener { result ->
                    val matchedRelatives = result.documents
                        .mapNotNull { it.toObject(Relative::class.java) }
                        .filter { relative ->
                            relative.fullName.contains(searchString)
                        }

                    continuation.resume(
                        Response(
                            200,
                            "Tìm thấy ${matchedRelatives.size} người thân",
                            matchedRelatives
                        )
                    )
                }
                .addOnFailureListener { e ->
                    continuation.resume(
                        Response(
                            500,
                            "Lỗi khi tìm kiếm người thân: ${e.localizedMessage}",
                            false
                        )
                    )
                }
        }

    override suspend fun insertRelativesRequestRemote(
        uid: String,
        relative: Relative
    ): Response<Any> = suspendCoroutine { continuation ->

        val docRef = db.collection("users")
            .document(uid)
            .collection("relatives_request")
            .document(relative.id)
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    continuation.resume(Response(409, "Yêu cầu đã tồn tại", false))
                } else {
                    docRef.set(relative)
                        .addOnSuccessListener {
                            continuation.resume(Response(200, "Gửi yêu cầu thành công", true))
                        }
                        .addOnFailureListener {
                            continuation.resume(Response(500, "Lỗi khi gửi yêu cầu", false))
                        }
                }
            }
            .addOnFailureListener {
                continuation.resume(Response(500, "Lỗi khi kiểm tra dữ liệu", false))
            }
    }

    override suspend fun getAllRelativeRequestRemote(uid: String): Response<Any> =
        suspendCoroutine { continuation ->
            db.collection("users")
                .document(uid)
                .collection("relatives_request")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val relatives =
                        querySnapshot.documents.mapNotNull { it.toObject(Relative::class.java) }
                    if (relatives.isEmpty()) {
                        continuation.resume(
                            Response(
                                204,
                                "Không có yêu cầu nào",
                                true
                            )
                        )
                    } else {
                        continuation.resume(
                            Response(
                                200,
                                "Lấy danh sách yêu cầu thành công",
                                relatives
                            )
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resume(
                        Response(
                            500,
                            "Lỗi khi lấy danh sách yêu cầu",
                            false,
                        )
                    )
                }
        }

    override suspend fun acceptRelativeRequestRemote(
        user: Account,
        relative: Relative
    ): Response<Any> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()

        val requestRef = db.collection("users")
            .document(user.id)
            .collection("relatives_request")
            .document(relative.id)

        val target1Ref = db.collection("users")
            .document(user.id)
            .collection("relatives")
            .document(relative.id)

        val target2Ref = db.collection("users")
            .document(relative.id)
            .collection("relatives")
            .document(user.id)

        val userToAddRef = db.collection("users").document(relative.id)

        userToAddRef.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    requestRef.delete()
                    continuation.resume(Response(404, "Người dùng không còn tồn tại", false))
                } else {
                    val reverseRelative = Relative(
                        user.id,
                        user.fullName,
                        "Bạn bè",
                        user.avatar
                    )
                    val write1 = target1Ref.set(relative)
                    val write2 = target2Ref.set(reverseRelative)

                    Tasks.whenAll(write1, write2)
                        .addOnSuccessListener {
                            requestRef.delete()
                                .addOnSuccessListener {
                                    continuation.resume(
                                        Response(200, "Chấp nhận lời mời thành công", true)
                                    )
                                }
                                .addOnFailureListener {
                                    continuation.resume(
                                        Response(
                                            200,
                                            "Chấp nhận thành công, nhưng lỗi khi xoá lời mời",
                                            true
                                        )
                                    )
                                }
                        }
                        .addOnFailureListener {
                            continuation.resume(
                                Response(500, "Lỗi khi tạo quan hệ hai chiều", false)
                            )
                        }
                }
            }
            .addOnFailureListener {
                continuation.resume(
                    Response(500, "Lỗi khi kiểm tra người dùng", false)
                )
            }
    }

    override suspend fun getAllReminderRelativeFromRemote(uid: String): Response<Any> {
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
                            val listSendTo =
                                list.filter { it.senderID == uid && it.receiverID != uid }
                            val listSendFrom =
                                list.filter { it.senderID != uid && it.receiverID == uid }
                            if (listSendTo.isNotEmpty() || listSendFrom.isNotEmpty()) {
                                val data = PillReminderResult(listSendTo, listSendFrom)
                                continuation.resume(
                                    Response(
                                        200,
                                        "Lấy danh sách nhắc thuốc thành công",
                                        data
                                    )
                                )
                            } else {
                                continuation.resume(
                                    Response(
                                        204,
                                        "Không có lời nhắc thuốc nào",
                                        PillReminderResult(emptyList(), emptyList())
                                    )
                                )
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

    override suspend fun updateReminderRelativeFromToRemote(
        reminderRelative: PillReminder
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()

            val toRef = db.collection("users")
                .document(reminderRelative.receiverID)
                .collection("pill_reminders")
                .document(reminderRelative.id)

            val fromRef = db.collection("users")
                .document(reminderRelative.senderID)
                .collection("pill_reminders")
                .document(reminderRelative.id)

            val task1 = toRef.set(reminderRelative, SetOptions.merge())
            val reminderCopy = reminderRelative
            val task2 = fromRef.set(reminderCopy, SetOptions.merge())

            val allTasks = mutableListOf(task1, task2)

            if (reminderRelative.statusRequest == ReminderRequestStatus.ACCEPTED.status) {
                reminderRelative.medicines.forEach { medicine ->
                    if (medicine.creatorID.isNotEmpty()) {
                        val medicineRef = db.collection("users")
                            .document(reminderRelative.receiverID)
                            .collection("medicines")
                            .document(medicine.id)

                        medicineRef.set(medicine, SetOptions.merge()).addOnSuccessListener {
                            Log.e("TAG", "updateReminderRelativeFromToRemote: success", )
                        }.addOnFailureListener {
                            Log.e("TAG", "updateReminderRelativeFromToRemote: success", )
                        }
                    }
                }
            }
            Tasks.whenAllComplete(allTasks)
                .addOnSuccessListener { tasks ->
                    val failedTasks = tasks.filter { !it.isSuccessful }
                    if (failedTasks.isEmpty()) {
                        continuation.resume(Response(200, "Cập nhật nhắc thuốc thành công", true))
                    } else {
                        val errorMsg = failedTasks.joinToString("\n") {
                            it.exception?.message ?: "Lỗi không xác định"
                        }
                        continuation.resume(
                            Response(
                                500,
                                "Một hoặc nhiều thao tác cập nhật thất bại: $errorMsg",
                                false
                            )
                        )
                    }
                }
                .addOnFailureListener {
                    continuation.resume(
                        Response(
                            500,
                            "Lỗi khi cập nhật nhắc thuốc: ${it.message}",
                            false
                        )
                    )
                }
        }
    }

    override suspend fun getReminderRelativeFromToByID(
        uid: String,
        reminderID: String
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid)
                .collection("pill_reminders")
                .document(reminderID)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val reminder =
                            document.toObject(PillReminder::class.java)?.apply { id = document.id }
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

    override suspend fun getRelativeHistoryByRelativeID(
        uid: String,
        relativeID: String
    ): Response<Any> = suspendCoroutine { continuation ->
        val relativeDocRef = db.collection("users")
            .document(uid)
            .collection("relatives")
            .document(relativeID)

        relativeDocRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val canSee = document.getBoolean("myCanSeeReminderHistory") ?: false
                    if (canSee) {
                        relativeDocRef.collection("reminder_history").get()
                            .addOnSuccessListener { result ->
                                val list = result.documents.mapNotNull { doc ->
                                    doc.toObject(ReminderHistory::class.java)?.apply { id = doc.id }
                                }

                                if (list.isNotEmpty()) {
                                    continuation.resume(
                                        Response(
                                            200,
                                            "Lấy dữ liệu thành công",
                                            list
                                        )
                                    )
                                } else {
                                    continuation.resume(
                                        Response(
                                            204,
                                            "Không có lịch sử nhắc nhở",
                                            true
                                        )
                                    )
                                }
                            }
                            .addOnFailureListener {
                                continuation.resume(
                                    Response(
                                        500,
                                        "Lỗi khi lấy lịch sử nhắc nhở",
                                        false
                                    )
                                )
                            }
                    } else {
                        continuation.resume(
                            Response(
                                403,
                                "Không có quyền xem lịch sử nhắc nhở",
                                false
                            )
                        )
                    }
                } else {
                    continuation.resume(Response(404, "Người thân không tồn tại", false))
                }
            }
            .addOnFailureListener {
                continuation.resume(Response(500, "Lỗi khi kiểm tra thông tin người thân", false))
            }
    }

    override suspend fun deleteRelativeReminderRemote(
        uid: String,
        idRelative: String,
        idReminder: String
    ): Response<Any> = suspendCoroutine { continuation ->
        val db = FirebaseFirestore.getInstance()
        val toRef = db.collection("users")
            .document(idRelative)
            .collection("pill_reminders")
            .document(idReminder)

        val fromRef = db.collection("users")
            .document(uid)
            .collection("pill_reminders")
            .document(idReminder)

        val task1 = toRef.delete()
        val task2 = fromRef.delete()

        Tasks.whenAllComplete(task1, task2)
            .addOnSuccessListener { tasks ->
                val failedTasks = tasks.filter { !it.isSuccessful }
                if (failedTasks.isEmpty()) {
                    continuation.resume(Response(200, "Xóa nhắc thuốc thành công", true))
                } else {
                    val errorMsg = failedTasks.joinToString("\n") {
                        it.exception?.message ?: "Lỗi không xác định"
                    }
                    continuation.resume(
                        Response(
                            500,
                            "Một hoặc nhiều thao tác xóa thất bại: $errorMsg",
                            false
                        )
                    )
                }
            }
            .addOnFailureListener {
                continuation.resume(
                    Response(
                        500,
                        "Lỗi khi xóa nhắc thuốc: ${it.message}",
                        false
                    )
                )
            }
    }
}