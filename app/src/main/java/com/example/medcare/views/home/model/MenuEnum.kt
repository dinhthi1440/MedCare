package com.example.medcare.views.home.model

enum class MenuItem(val icon: String, val title: String) {
    MEDICINE("💊", "Thuốc của tôi"),
    REMINDER("⏰", "Nhắc uống thuốc"),
    HISTORY("📜", "Lịch sử dùng thuốc"),
    FAMILY("👨‍👩‍👧", "Kết nối người thân"),
    CONSULT("🩺", "Tư vấn sức khỏe"),
    APPOINTMENT("📅", "Đặt lịch khám");

    companion object {
        fun getAllItems() = values().toList()
    }
}
