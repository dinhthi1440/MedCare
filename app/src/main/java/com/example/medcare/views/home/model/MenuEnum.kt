package com.example.medcare.views.home.model

enum class MenuItem(val icon: String, val title: String) {
    MEDICINE("💊", "Thuốc của tôi"),
    REMINDER("⏰", "Nhắc uống thuốc"),
    HISTORY("📜", "Lịch sử dùng thuốc"),
    FAMILY("👨‍👩‍👧", "Kết nối người thân"),
    CONSULT("🩺", "Tư vấn sức khỏe"),
    APPOINTMENT("📅", "Đặt lịch khám"),
    USER_MANAGEMENT("👤", "Quản lý tài khoản"),
    FEEDBACK_MANAGEMENT("💬", "Quản lý phản hồi");

    companion object {
        fun getAllItems() = values().toList()

        fun getUserItems(): List<MenuItem> = listOf(
            MEDICINE, REMINDER, HISTORY, FAMILY, CONSULT, APPOINTMENT
        )

        fun getAdminItems(): List<MenuItem> = listOf(
            USER_MANAGEMENT, FEEDBACK_MANAGEMENT
        )
    }
}
