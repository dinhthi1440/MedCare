package com.example.medcare.data.datasource.relatives

import com.example.medcare.models.Relative
import com.example.medcare.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RelativesDataSource: IRelativesDataSource {
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
                                continuation.resume(Response(200, "Lấy danh sách người thân thành công", list))
                            } else {
                                continuation.resume(Response(204, "Không có người thân nào", emptyList<Relative>()))
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
                                continuation.resume(Response(200, "Huỷ kết nối với người thân thành công", true))
                            }
                            .addOnFailureListener {
                                continuation.resume(Response(500, "Lỗi khi xoá", false))
                            }
                    } else {
                        continuation.resume(Response(404, "Không tìm thấy người thân để xoá", false))
                    }
                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi kiểm tra người thân", false))
                }
        }
    }

    override suspend fun getSearchRelativesRemote(searchString: String): Response<Any> = suspendCoroutine { continuation ->
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
        uid: String,
        relative: Relative
    ): Response<Any> {
        return suspendCoroutine { continuation ->
            val db = FirebaseFirestore.getInstance()

            val requestRef = db.collection("users")
                .document(uid)
                .collection("relatives_request")
                .document(relative.id)

            val targetRef = db.collection("users")
                .document(uid)
                .collection("relatives")
                .document(relative.id)

            val userToAddRef = db.collection("users")
                .document(relative.id)

            // Bước 1: Kiểm tra người được thêm có tồn tại không
            userToAddRef.get()
                .addOnSuccessListener { snapshot ->
                    if (!snapshot.exists()) {
                        // Người dùng không tồn tại → xoá request
                        requestRef.delete()
                        continuation.resume(Response(404, "Người dùng không còn tồn tại", false))
                    } else {
                        // Bước 2: Người tồn tại → thêm vào relatives
                        targetRef.set(relative)
                            .addOnSuccessListener {
                                // Bước 3: Xoá request nếu thêm thành công
                                requestRef.delete()
                                    .addOnSuccessListener {
                                        continuation.resume(
                                            Response(
                                                200,
                                                "Chấp nhận lời mời thành công",
                                                true
                                            )
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
                                continuation.resume(Response(500, "Lỗi khi thêm người thân", false))
                            }
                    }


                }
                .addOnFailureListener {
                    continuation.resume(Response(500, "Lỗi khi kiểm tra người dùng", false))
                }
        }
    }



}