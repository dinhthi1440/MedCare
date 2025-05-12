package com.example.medcare.views.feedback.feedback_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.models.Feedback

class FeedbackViewModel : BaseViewModel() {
    val feedbackList = listOf(
        Feedback(
            id = "fb001",
            content = "Ứng dụng đôi lúc bị treo khi thêm thuốc.",
            senderID = "acc001",
            senderName = "Nguyễn Văn A",
            senderAvatar = "https://example.com/avatar/a.jpg",
            date = "2025-05-01",
            time = "10:15",
            status = "pending"
        ),
        Feedback(
            id = "fb002",
            content = "Nên thêm tính năng ghi chú khi nhắc uống thuốc.",
            senderID = "acc002",
            senderName = "Trần Thị B",
            senderAvatar = "https://example.com/avatar/b.jpg",
            date = "2025-05-02",
            time = "09:30",
            status = "processing"
        ),
        Feedback(
            id = "fb003",
            content = "Không nhận được thông báo nhắc nhở.",
            senderID = "acc004",
            senderName = "Phạm Thị D",
            senderAvatar = "https://example.com/avatar/d.jpg",
            date = "2025-05-03",
            time = "14:45",
            status = "resolved"
        ),
        Feedback(
            id = "fb004",
            content = "Rất hài lòng với giao diện của ứng dụng!",
            senderID = "acc005",
            senderName = "Đỗ Mạnh E",
            senderAvatar = "https://example.com/avatar/e.jpg",
            date = "2025-05-03",
            time = "16:00",
            status = "resolved"
        ),
        Feedback(
            id = "fb005",
            content = "Tài khoản tôi bị khoá mà không rõ lý do.",
            senderID = "acc003",
            senderName = "Lê Văn C",
            senderAvatar = "https://example.com/avatar/c.jpg",
            date = "2025-05-04",
            time = "11:20",
            status = "pending"
        )
    )
    val getFeedbacks: LiveData<MutableList<Feedback>> get() = _setFeedbacks
    private val _setFeedbacks = MutableLiveData<MutableList<Feedback>>()
    fun getFeedbackList() {
        _setFeedbacks.value = feedbackList.toMutableList()
    }
}