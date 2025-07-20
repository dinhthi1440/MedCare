package com.example.medcare.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class TimeUtils {

    companion object {
        fun timeUntil(targetTimeStr: String): String {
            val format = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
            format.timeZone = TimeZone.getDefault()

            return try {
                val pastTime = format.parse(targetTimeStr)
                val now = Date()

                if (pastTime == null) return "Định dạng không hợp lệ"

                val diffInMillis = now.time - pastTime.time

                if (diffInMillis <= 0) {
                    "Thời gian đã qua"
                } else {
                    val diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis)
                    val diffHours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
                    val diffDays = TimeUnit.MILLISECONDS.toDays(diffInMillis)

                    return when {
                        diffMinutes < 60 -> "$diffMinutes phút"
                        diffHours < 24 -> "$diffHours giờ"
                        else -> "$diffDays ngày"
                    }
                }
            } catch (e: Exception) {
                "Lỗi khi xử lý thời gian"
            }
        }
        fun formatTimestamp(timestampMillis: Long): String {
            val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
            val date = Date(timestampMillis)
            return sdf.format(date)
        }
        fun getCurrentCreatedAt(): String {
            val format = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
            format.timeZone = TimeZone.getDefault()
            return format.format(Date())
        }
        fun getCurrentTime(): String {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            format.timeZone = TimeZone.getDefault()
            return format.format(Date())
        }
        fun getCurrentDate(): String {
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            format.timeZone = TimeZone.getDefault()
            return format.format(Date())
        }
    }
}