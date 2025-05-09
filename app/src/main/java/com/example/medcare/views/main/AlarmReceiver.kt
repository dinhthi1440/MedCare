package com.example.medcare.views.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.e("2222222", "onReceive: Nhậncx được receiver", )
        val alarmID = intent?.getStringExtra("alarm_id") ?: 0
        val reminderID = intent?.getStringExtra("reminder_id") ?: 0
        val alarmMessage = intent?.getStringExtra("alarm_message") ?: 0
        val i = Intent(context, AlertActivity::class.java).apply {
            putExtra("alarm_id", alarmID)
            putExtra("reminder_id", reminderID)
            putExtra("alarm_message", alarmMessage)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        context.startActivity(i)
    }
}