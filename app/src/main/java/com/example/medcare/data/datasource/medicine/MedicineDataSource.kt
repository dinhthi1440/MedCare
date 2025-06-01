package com.example.medcare.data.datasource.medicine

import com.example.medcare.data.database.local.DataBaseLocal
import com.example.medcare.models.Account
import com.example.medcare.models.Medicine
import com.example.medcare.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MedicineDataSource(private val dataBaseLocal: DataBaseLocal) : IMedicineDataSource {
    override suspend fun insertMedicine(medicine: Medicine): Long {
        return dataBaseLocal.medicineDao.insertMedicine(medicine)
    }

    override suspend fun getAllMedicine(): List<Medicine> {
        return dataBaseLocal.medicineDao.getAllMedicine()
    }

    override suspend fun deleteMedicine(idMedicine: String): Int {
        return dataBaseLocal.medicineDao.deleteMedicine(idMedicine)
    }

    override suspend fun getMedicineById(idMedicine: String): Medicine {
        return dataBaseLocal.medicineDao.getMedicineById(idMedicine)
    }

    override suspend fun updateMedicine(medicine: Medicine): Int {
        return dataBaseLocal.medicineDao.updateMedicine(medicine)
    }

    override suspend fun insertMedicineRemote(uid: String, medicine: Medicine): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid).collection("medicines").document(medicine.id)
                .set(medicine)
                .addOnSuccessListener {
                    continuation.resume(Response(200, "Tạo dữ liệu thuốc thành công", true))
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Tạo dữ liệu thất bại: ${it.message}", false))
                }
        }
    }

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

                    if (medicineList.isEmpty()) {
                        continuation.resume(
                            Response(
                                204,
                                "Không có thuốc nào trong danh sách",
                                emptyList<Medicine>()
                            )
                        )
                    } else {
                        continuation.resume(
                            Response(
                                200,
                                "Lấy danh sách thuốc thành công",
                                medicineList
                            )
                        )
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

    override suspend fun getMedicineByIdRemote(uid: String, medicineID: String): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(uid)
                .collection("medicines")
                .document(medicineID)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val medicine =
                            document.toObject(Medicine::class.java)?.apply { id = document.id }
                        continuation.resume(Response(200, "Lấy thuốc thành công", medicine))
                    } else {
                        continuation.resume(
                            Response(
                                404,
                                "Không tìm thấy thuốc, có thể thuốc đã bị xoá",
                                null
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Response(500, "Lỗi Không xác định", null))
                }
        }
    }

    override suspend fun updateMedicineRemote(uid: String, medicine: Medicine): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("users")
                .document(uid)
                .collection("medicines")
                .document(medicine.id)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.set(medicine)
                            .addOnSuccessListener {
                                continuation.resume(
                                    Response(
                                        200,
                                        "Cập nhật thuốc thành công",
                                        true
                                    )
                                )
                            }
                            .addOnFailureListener { _ ->
                                continuation.resume(Response(500, "Lỗi khi cập nhật", false))
                            }
                    } else {
                        continuation.resume(
                            Response(
                                404,
                                "Không tìm thấy thuốc để cập nhật",
                                false
                            )
                        )
                    }
                }
                .addOnFailureListener { _ ->
                    continuation.resume(Response(500, "Lỗi khi kiểm tra thuốc", false))
                }
        }
    }

    override suspend fun deleteMedicineRemote(uid: String, medicineID: String): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("users")
                .document(uid)
                .collection("medicines")
                .document(medicineID)

            docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        docRef.delete()
                            .addOnSuccessListener {
                                continuation.resume(Response(200, "Xoá thuốc thành công", true))
                            }
                            .addOnFailureListener { _ ->
                                continuation.resume(
                                    Response(
                                        500,
                                        "Lỗi khi xoá, hãy thử lại",
                                        false
                                    )
                                )
                            }
                    } else {
                        continuation.resume(Response(404, "Không tìm thấy thuốc để xoá", false))
                    }
                }
                .addOnFailureListener { _ ->
                    continuation.resume(Response(500, "Lỗi khi kiểm tra thuốc", false))
                }
        }
    }
}