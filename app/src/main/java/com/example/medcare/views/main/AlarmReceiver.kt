package com.example.medcare.views.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MedCare:AlarmReceiverWakeLock").apply {
            acquire(60 * 1000L /* 1 phút */)
        }

        try {
            intent?.let { receivedIntent ->
                Log.e("AlarmReceiver", "onReceive: Nhận được receiver")
                val alarmID = receivedIntent.getStringExtra("alarm_id") ?: ""
                val reminderID = receivedIntent.getStringExtra("reminder_id") ?: ""
                val alarmMessage = receivedIntent.getStringExtra("alarm_message") ?: ""

                val alertActivityIntent = Intent(context, AlertActivity::class.java).apply {
                    putExtra("alarm_id", alarmID)
                    putExtra("reminder_id", reminderID)
                    putExtra("alarm_message", alarmMessage)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                context.startActivity(alertActivityIntent)
            } ?: run {
                Log.e("AlarmReceiver", "onReceive: Intent is null. Cannot process alarm.")
            }
        } finally {
            // Đảm bảo wake lock được release, ngay cả khi có lỗi xảy ra
            if (wakeLock.isHeld) {
                wakeLock.release()
            }
        }
    }
}