package com.example.medcare.data.datasource.doctor

import com.example.medcare.models.ChatMessage
import com.example.medcare.models.Doctor
import com.example.medcare.models.DoctorChat
import com.example.medcare.models.ReminderHistory
import com.example.medcare.models.Response
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DoctorDatasource : IDoctorDatasource {
    private val db = FirebaseFirestore.getInstance()
    private val collectionInUser = "chat_doctors"
    override suspend fun getAllDoctorChat(uid: String): Response<Any> {
        return suspendCoroutine { continuation ->
            try {
                db.collection("users")
                    .document(uid)
                    .collection(collectionInUser)
                    .get()
                    .addOnSuccessListener { result ->
                        try {
                            val list = result.documents.mapNotNull { doc ->
                                doc.toObject(DoctorChat::class.java)?.apply { id = doc.id }
                            }

                            if (list.isNotEmpty()) {
                                continuation.resume(
                                    Response(
                                        200,
                                        "Lấy danh sách bác sĩ thành công",
                                        list
                                    )
                                )
                            } else {
                                continuation.resume(
                                    Response(
                                        204,
                                        "Không có bác sĩ nào",
                                        emptyList<ReminderHistory>()
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            continuation.resume(
                                Response(
                                    500,
                                    "Lỗi xử lý dữ liệu bác sĩ",
                                    null
                                )
                            )
                        }
                    }
                    .addOnFailureListener { _ ->
                        continuation.resume(
                            Response(
                                500,
                                "Lỗi khi lấy danh sách bác sĩ",
                                null
                            )
                        )
                    }
            } catch (e: Exception) {
                continuation.resume(Response(500, "Lỗi khi kết nối tới Firestore", null))
            }
        }
    }

    override suspend fun getSearchDoctorChat(uid: String, search: String): Response<Any> {
        return suspendCoroutine { continuation ->
            try {
                val usersRef = db.collection("users")
                usersRef
                    .whereEqualTo("rule", "doctor")
                    .whereEqualTo("fullName", search)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val doctorList = querySnapshot.documents.mapNotNull { doc ->
                            doc.toObject(Doctor::class.java)?.apply { id = doc.id }
                        }
                        continuation.resume(Response(200, "Tìm thấy ${doctorList.size} bác sĩ", doctorList))
                    }
                    .addOnFailureListener {
                        continuation.resume(Response(500, "Lỗi khi truy vấn dữ liệu", null))
                    }
            } catch (e: Exception) {
                continuation.resume(Response(500, "Lỗi hệ thống",  null))
            }
        }
    }

    override suspend fun getAllDoctor(): Response<Any> {
        return suspendCoroutine { continuation ->
            try {
                val usersRef = db.collection("users")
                usersRef
                    .whereEqualTo("rule", "doctor")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val doctorList = querySnapshot.documents.mapNotNull { doc ->
                            doc.toObject(Doctor::class.java)?.apply { id = doc.id }
                        }
                        continuation.resume(Response(200, "Tìm thấy ${doctorList.size} bác sĩ", doctorList))
                    }
                    .addOnFailureListener {
                        continuation.resume(Response(500, "Lỗi khi truy vấn dữ liệu", null))
                    }
            } catch (e: Exception) {
                continuation.resume(Response(500, "Lỗi hệ thống",  null))
            }
        }
    }

    override suspend fun getChatDetail(uid: String, partnerID: String): Response<Any> {
        return suspendCoroutine { continuation ->
            try {
                db.collection("users")
                    .document(uid)
                    .collection(collectionInUser)
                    .document(partnerID)
                    .collection("chat_messages")
                    .get()
                    .addOnSuccessListener { result ->
                        try {
                            val list = result.documents.mapNotNull { doc ->
                                doc.toObject(ChatMessage::class.java)?.apply { id = doc.id }
                            }

                            if (list.isNotEmpty()) {
                                continuation.resume(
                                    Response(
                                        200,
                                        "Lấy danh sách tin nhắn thành công",
                                        list
                                    )
                                )
                            } else {
                                continuation.resume(
                                    Response(
                                        204,
                                        "Hãy bắt đầu tin nhắn nào!",
                                        emptyList<ReminderHistory>()
                                    )
                                )
                            }
                        } catch (e: Exception) {
                            continuation.resume(
                                Response(
                                    500,
                                    "Lỗi xử lý dữ liệu tin nhắn",
                                    null
                                )
                            )
                        }
                    }
                    .addOnFailureListener { _ ->
                        continuation.resume(
                            Response(
                                500,
                                "Lỗi khi lấy danh sách tin nhắn",
                                null
                            )
                        )
                    }
            } catch (e: Exception) {
                continuation.resume(Response(500, "Lỗi khi kết nối tới Firestore", null))
            }
        }
    }

    override suspend fun insertChatMessage(
        uid: String,
        partnerID: String,
        chatMessage: ChatMessage,
        doctorChat: DoctorChat
    ): Response<Any> = withContext(Dispatchers.IO) {
        try {
            val senderRef = db.collection("users")
                .document(uid)
                .collection(collectionInUser)
                .document(partnerID)

            val receiverRef = db.collection("users")
                .document(partnerID)
                .collection(collectionInUser)
                .document(uid)

            val batch = db.batch()

            val senderDoc = senderRef.get().await()
            if (!senderDoc.exists()) {
                batch.set(senderRef, doctorChat)
            } else {
                batch.update(senderRef, mapOf(
                    "lastMessage" to doctorChat.lastMessage,
                    "lastMessageSenderID" to doctorChat.lastMessageSenderID,
                    "timeLastMessage" to doctorChat.timeLastMessage,
                    "readLastMessage" to doctorChat.isReadLastMessage,
                ))
            }

            val receiverDoc = receiverRef.get().await()
            if (!receiverDoc.exists()) {
                batch.set(receiverRef, doctorChat)
            } else {
                batch.update(receiverRef, mapOf(
                    "lastMessage" to doctorChat.lastMessage,
                    "lastMessageSenderID" to doctorChat.lastMessageSenderID,
                    "timeLastMessage" to doctorChat.timeLastMessage,
                    "readLastMessage" to doctorChat.isReadLastMessage,
                ))
            }

            val messageSender = senderRef.collection("chat_messages").document(chatMessage.id)
            val messageReceiver = receiverRef.collection("chat_messages").document(chatMessage.id)

            batch.set(messageSender, chatMessage)
            batch.set(messageReceiver, chatMessage)

            batch.commit().await()

            return@withContext Response(200, "Thêm thành công", null)
        } catch (e: Exception) {
            return@withContext Response(500, "Lỗi: ${e.message}", null)
        }
    }


}