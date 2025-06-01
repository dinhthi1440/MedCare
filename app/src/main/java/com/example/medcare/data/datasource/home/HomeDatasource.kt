package com.example.medcare.data.datasource.home

import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.Medicine
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.ReminderRequestStatus
import com.example.medcare.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HomeDatasource(private val dataBaseLocal: DataBaseLocal) : IHomeDatasource{

    override suspend fun getAllMedicineRemote(uid: String): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid)
                .collection("medicines")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val medicineList = querySnapshot.documents.mapNotNull { document ->
                        document.toObject(Medicine::class.java)?.apply { id = document.id }
                    }

                    continuation.resume(
                        Response(
                            200,
                            "Lấy danh sách thuốc thành công",
                            medicineList
                        )
                    )
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

    override suspend fun getAllHistory(): List<ReminderHistory> {
        return dataBaseLocal.historyDAO.getAllMedicine()
    }

    override suspend fun deleteAllHistory(): Int {
        return dataBaseLocal.historyDAO.deleteAllHistory()
    }

    override suspend fun insertHistoryRemote(
        uid: String,
        reminderHistories: List<ReminderHistory>
    ): Response<Any> {
        val db = FirebaseFirestore.getInstance()

        return suspendCoroutine { continuation ->
            val collectionRef = db.collection("users")
                .document(uid)
                .collection("reminder_history")

            // Lưu những cái cần insert
            val insertList = mutableListOf<ReminderHistory>()

            // Đếm kiểm tra xong tất cả mới tiếp tục
            var checkedCount = 0
            val total = reminderHistories.size

            if (total == 0) {
                continuation.resume(Response(200, "Không có dữ liệu cần insert", true))
                return@suspendCoroutine
            }

            for (history in reminderHistories) {
                val docRef = collectionRef.document(history.id)
                docRef.get()
                    .addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            insertList.add(history)
                        }
                        checkedCount++
                        if (checkedCount == total) {
                            // Sau khi kiểm tra xong hết
                            if (insertList.isEmpty()) {
                                continuation.resume(Response(200, "Không có lịch sử mới để insert", true))
                            } else {
                                val batch = db.batch()
                                for (item in insertList) {
                                    val newDocRef = collectionRef.document(item.id)
                                    batch.set(newDocRef, item)
                                }
                                batch.commit()
                                    .addOnSuccessListener {
                                        continuation.resume(
                                            Response(200, "Thêm ${insertList.size} lịch sử mới thành công", true)
                                        )
                                    }
                                    .addOnFailureListener {
                                        continuation.resume(
                                            Response(500, "Lỗi khi insert lịch sử mới", false)
                                        )
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        continuation.resume(Response(500, "Lỗi kiểm tra tồn tại document", false))
                        return@addOnFailureListener
                    }
            }
        }
    }

    override suspend fun getAllMedicine(): List<Medicine> {
        return dataBaseLocal.medicineDao.getAllMedicine()
    }

    override suspend fun getAllPillReminder(): List<PillReminder> {
        return dataBaseLocal.pillReminderDAO.getAllPillReminder()
    }


    override suspend fun insertAllMedicine(medicine: List<Medicine>): List<Long> {
        return dataBaseLocal.medicineDao.insertAllMedicine(medicine)
    }

    override suspend fun deleteAllMedicine(): Int {
        return dataBaseLocal.medicineDao.deleteAllMedicine()
    }

    override suspend fun deleteAllReminder(): Int {
        return dataBaseLocal.pillReminderDAO.deleteAllReminder()
    }

    override suspend fun insertAllReminder(pillReminder: List<PillReminder>): List<Long> {
        return dataBaseLocal.pillReminderDAO.insertAllReminder(pillReminder)
    }


    override suspend fun getAllReminderRemote(uid: String): Response<Any> {
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
                            }.filter {
                                (it.senderID == uid && it.receiverID == uid) ||
                                        (it.receiverID == uid && it.statusRequest == ReminderRequestStatus.ACCEPTED.status)
                            }

                            continuation.resume(
                                Response(
                                    200,
                                    "Lấy danh sách nhắc thuốc thành công",
                                    list
                                )
                            )
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
}