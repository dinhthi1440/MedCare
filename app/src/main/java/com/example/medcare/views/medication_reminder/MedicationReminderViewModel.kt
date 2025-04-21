package com.example.medcare.views.medication_reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.views.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.medication_reminder.model.PillReminder

class MedicationReminderViewModel: BaseViewModel() {
    private val _getReminderList = MutableLiveData<MutableList<PillReminder>>()
    val getReminderList: LiveData<MutableList<PillReminder>> get() = _getReminderList

    fun getReminderList() {
        val fakePillReminders = mutableListOf(
            PillReminder(
                id = "1",
                label = "Morning Routine",
                times = "08:00",
                frequency = "Daily",
                medicines = "Vitamin C, Aspirin, Vitamin C, Aspirin, Vitamin C, Aspirin",
                isOn = true
            ),
            PillReminder(
                id = "2",
                label = "Afternoon Dose",
                times = "13:00",
                frequency = "Every 2 days",
                medicines = "Amoxicillin",
                isOn = true
            ),
            PillReminder(
                id = "3",
                label = "Evening Meds",
                times = "19:00",
                frequency = "Daily",
                medicines = "Metformin, Lisinopril",
                isOn = false
            ),
            PillReminder(
                id = "4",
                label = "Before Sleep",
                times = "22:00",
                frequency = "Weekly",
                medicines = "Melatonin",
                isOn = false
            )
        )
        _getReminderList.value = fakePillReminders
    }


}