package com.example.medcare.models

data class PillReminderResult(
    val listSendTo: List<PillReminder>,
    val listSendFrom: List<PillReminder>
)
