package com.example.medcare.views.medication_reminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.models.Medicine
import com.example.medcare.views.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.add_new_reminder.model.SelectedTime
import com.example.medcare.views.medication_reminder.model.PillReminder

class MedicationReminderViewModel(private val iPillReminderRepos: IPillReminderRepos.Local): BaseViewModel() {
    private val _setReminderList = MutableLiveData<MutableList<PillReminder>>()
    val getReminderList: LiveData<MutableList<PillReminder>> get() = _setReminderList
    private val _setPatchStatus = MutableLiveData<Boolean>()
    val getPatchStatus: LiveData<Boolean> get() = _setPatchStatus

    fun getReminderList() {
        val fakePillReminders = mutableListOf(
            PillReminder(
                id = "1",
                label = "Morning Routine",
                times = listOf(
                    SelectedTime(id = 1, time = "08:00", amPm = "AM")
                ),
                frequency = FrequencyModel(
                    id = 1,
                    label = "Daily",
                    isSelected = true,
                    listDateSelected = null
                ),
                medicines = listOf(
                    Medicine(id = "m1", name = "Vitamin C", quantity = 1, unit = "viên"),
                    Medicine(id = "m2", name = "Aspirin", quantity = 1, unit = "viên"),
                    Medicine(id = "m3", name = "Vitamin C", quantity = 1, unit = "viên"),
                    Medicine(id = "m4", name = "Aspirin", quantity = 1, unit = "viên"),
                    Medicine(id = "m5", name = "Vitamin C", quantity = 1, unit = "viên"),
                    Medicine(id = "m6", name = "Aspirin", quantity = 1, unit = "viên")
                ),
                isOn = true,
                note = "Uống trước khi ăn",
                disease = "Cảm"
            ),
            PillReminder(
                id = "2",
                label = "Afternoon Dose",
                times = listOf(
                    SelectedTime(id = 2, time = "13:00", amPm = "PM")
                ),
                frequency = FrequencyModel(
                    id = 2,
                    label = "Every 2 days",
                    isSelected = true,
                    listDateSelected = null
                ),
                medicines = listOf(
                    Medicine(id = "m7", name = "Amoxicillin", quantity = 2, unit = "viên")
                ),
                isOn = true,
                note = "Sau bữa trưa",
                disease = "Viêm họng"
            ),
            PillReminder(
                id = "3",
                label = "Evening Meds",
                times = listOf(
                    SelectedTime(id = 3, time = "19:00", amPm = "PM")
                ),
                frequency = FrequencyModel(
                    id = 3,
                    label = "Daily",
                    isSelected = true,
                    listDateSelected = null
                ),
                medicines = listOf(
                    Medicine(id = "m8", name = "Metformin", quantity = 1, unit = "viên"),
                    Medicine(id = "m9", name = "Lisinopril", quantity = 1, unit = "viên")
                ),
                isOn = false,
                note = "Sau khi ăn tối",
                disease = "Tiểu đường"
            ),
            PillReminder(
                id = "4",
                label = "Before Sleep",
                times = listOf(
                    SelectedTime(id = 4, time = "22:00", amPm = "PM")
                ),
                frequency = FrequencyModel(
                    id = 4,
                    label = "Weekly",
                    isSelected = true,
                    listDateSelected = null
                ),
                medicines = listOf(
                    Medicine(id = "m10", name = "Melatonin", quantity = 1, unit = "viên")
                ),
                isOn = false,
                note = "Uống 30 phút trước khi ngủ",
                disease = "Mất ngủ"
            )
        )
        executeTask(
            request = {iPillReminderRepos.getAllPillReminder()},
            onSuccess = {
                _setReminderList.value = it.toMutableList()
                _setPatchStatus.value = true
            },
            onError = {
                _setPatchStatus.value = false
            }
        )

    }


}