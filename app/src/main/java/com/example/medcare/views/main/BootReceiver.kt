package com.example.medcare.views.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.e("2222222", "onReceive: Nhậncx được receiver", )
        val alarmID = p1?.getStringExtra("alarm_id") ?: ""
        val reminderID = p1?.getStringExtra("reminder_id") ?: ""
        val alarmMessage = p1?.getStringExtra("alarm_message") ?: ""
        val i = Intent(p0, AlertActivity::class.java).apply {
            putExtra("alarm_id", alarmID)
            putExtra("reminder_id", reminderID)
            putExtra("alarm_message", alarmMessage)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        p0?.startActivity(i)
    }
}