package com.example.medcare.views.health_advice.chat_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.models.ChatMessage
import com.example.medcare.models.Doctor
import com.example.medcare.models.DoctorChat

class ChatDoctorViewModel : BaseViewModel() {
    val chatList = listOf(
        DoctorChat(
            id = "chat1",
            patientID = "p1",
            patientName = "Nguyễn Văn An",
            patientAvatar = "https://example.com/p1.jpg",
            doctorID = "d1",
            doctorName = "BS. Trần Văn Bình",
            doctorAvatar = "https://example.com/d1.jpg",
            lastMessage = "Chào bác sĩ, tôi bị đau họng",
            timeLastMessage = "2025-05-11T08:30:00",
            isReadLastMessage = false,
            timeReadLastMessage = "",
            isPatientSendLastMessage = true
        ),
        DoctorChat(
            id = "chat2",
            patientID = "p2",
            patientName = "Lê Thị Hoa",
            patientAvatar = "https://example.com/p2.jpg",
            doctorID = "d1",
            doctorName = "BS. Trần Văn Bình",
            doctorAvatar = "https://example.com/d1.jpg",
            lastMessage = "Vâng, tôi sẽ dùng thuốc đều đặn",
            timeLastMessage = "2025-05-10T21:45:00",
            isReadLastMessage = true,
            timeReadLastMessage = "2025-05-10T21:50:00",
            isPatientSendLastMessage = false
        ),
        DoctorChat(
            id = "chat3",
            patientID = "p3",
            patientName = "Phạm Minh Long",
            patientAvatar = "https://example.com/p3.jpg",
            doctorID = "d2",
            doctorName = "BS. Nguyễn Thị Lan",
            doctorAvatar = "https://example.com/d2.jpg",
            lastMessage = "Tôi đã có kết quả xét nghiệm",
            timeLastMessage = "2025-05-09T10:15:00",
            isReadLastMessage = false,
            timeReadLastMessage = "",
            isPatientSendLastMessage = true
        ),
        DoctorChat(
            id = "chat4",
            patientID = "p4",
            patientName = "Đỗ Văn Tuấn",
            patientAvatar = "https://example.com/p4.jpg",
            doctorID = "d3",
            doctorName = "BS. Lê Hồng Sơn",
            doctorAvatar = "https://example.com/d3.jpg",
            lastMessage = "Cảm ơn bác sĩ, tôi thấy đỡ hơn",
            timeLastMessage = "2025-05-08T17:00:00",
            isReadLastMessage = true,
            timeReadLastMessage = "2025-05-08T17:05:00",
            isPatientSendLastMessage = true
        ),
        DoctorChat(
            id = "chat5",
            patientID = "p5",
            patientName = "Trần Thị Mai",
            patientAvatar = "https://example.com/p5.jpg",
            doctorID = "d4",
            doctorName = "BS. Phạm Văn Hùng",
            doctorAvatar = "https://example.com/d4.jpg",
            lastMessage = "Bác sĩ có thể gọi video không?",
            timeLastMessage = "2025-05-07T14:25:00",
            isReadLastMessage = false,
            timeReadLastMessage = "",
            isPatientSendLastMessage = true
        )
    )

    val chatMessages = listOf(
        ChatMessage(
            id = "msg1",
            senderID = "userA",
            content = "Chào bác sĩ, tôi có một câu hỏi.",
            timeMessage = "2025-05-11T08:15:00",
            timeRead = "",
            isRead = false
        ),
        ChatMessage(
            id = "msg2",
            senderID = "userB",
            content = "Xin chào, anh có thể nói rõ hơn được không?",
            timeMessage = "2025-05-11T08:16:30",
            timeRead = "2025-05-11T08:17:00",
            isRead = true
        ),
        ChatMessage(
            id = "msg3",
            senderID = "userA",
            content = "Tôi bị đau đầu và ho khan khoảng 2 ngày rồi.",
            timeMessage = "2025-05-11T08:17:30",
            timeRead = "",
            isRead = false
        ),
        ChatMessage(
            id = "msg4",
            senderID = "userB",
            content = "Anh có bị sốt không?",
            timeMessage = "2025-05-11T08:18:10",
            timeRead = "",
            isRead = false
        ),
        ChatMessage(
            id = "msg5",
            senderID = "userA",
            content = "Dạ có, sốt nhẹ vào ban đêm.",
            timeMessage = "2025-05-11T08:19:00",
            timeRead = "",
            isRead = false
        )
    )

    val getDoctorChats: LiveData<MutableList<DoctorChat>> get() = _setDoctorChats
    private val _setDoctorChats = MutableLiveData<MutableList<DoctorChat>>()

    val getChatDetail: LiveData<MutableList<ChatMessage>> get() = _setChatDetail
    private val _setChatDetail = MutableLiveData<MutableList<ChatMessage>>()

    fun getChatList() {
        _setDoctorChats.value = chatList.toMutableList()
    }

    fun getChatDetail() {
        _setChatDetail.value = chatMessages.toMutableList()
    }
}