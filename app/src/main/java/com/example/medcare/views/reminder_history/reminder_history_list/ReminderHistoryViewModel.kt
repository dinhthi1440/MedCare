package com.example.medcare.views.reminder_history.reminder_history_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.models.Medicine
import com.example.medcare.models.ReminderHistory
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime
import com.example.medcare.models.PillReminder

class ReminderHistoryViewModel : BaseViewModel() {
    val sampleReminderHistories = mutableListOf(
        ReminderHistory(
            id = "rh1",
            label = "Nhắc uống sáng",
            date = "2025-05-09",
            time = "08:00",
            status = "Chưa xác nhận",
            reminder = PillReminder(
                id = "r1",
                label = "Uống thuốc đau đầu",
                times = listOf(SelectedTime(1, "08:00")),
                frequency = FrequencyModel(1, "Hằng ngày", true),
                medicines = listOf(
                    Medicine(id = "m1", name = "Paracetamol", dosage = 1, unit = "viên")
                ),
                isOn = true,
                note = "Sau khi ăn sáng",
                disease = "Đau đầu"
            )
        ),
        ReminderHistory(
            id = "rh2",
            label = "Nhắc uống trưa",
            date = "2025-05-09",
            time = "12:00",
            status = "Đã uống",
            reminder = PillReminder(
                id = "r2",
                label = "Uống thuốc cảm",
                times = listOf(SelectedTime(2, "12:00")),
                frequency = FrequencyModel(2, "Hằng ngày", true),
                medicines = listOf(
                    Medicine(id = "m2", name = "Decolgen", dosage = 1, unit = "viên")
                ),
                isOn = true,
                note = "Trước khi ăn trưa",
                disease = "Cảm lạnh"
            )
        ),
        ReminderHistory(
            id = "rh3",
            label = "Nhắc uống chiều",
            date = "2025-05-08",
            time = "16:00",
            status = "Bỏ lỡ",
            reminder = PillReminder(
                id = "r3",
                label = "Uống thuốc kháng sinh",
                times = listOf(SelectedTime(3, "16:00")),
                frequency = FrequencyModel(3, "2 lần/ngày", true),
                medicines = listOf(
                    Medicine(id = "m3", name = "Amoxicillin", dosage = 1, unit = "viên")
                ),
                isOn = true,
                note = "Uống đủ liều",
                disease = "Nhiễm khuẩn"
            )
        ),
        ReminderHistory(
            id = "rh4",
            label = "Nhắc uống tối",
            date = "2025-05-08",
            time = "20:00",
            status = "Đã uống",
            reminder = PillReminder(
                id = "r4",
                label = "Uống vitamin",
                times = listOf(SelectedTime(4, "20:00")),
                frequency = FrequencyModel(4, "1 lần/ngày", true),
                medicines = listOf(
                    Medicine(id = "m4", name = "Vitamin C", dosage = 1, unit = "viên")
                ),
                isOn = true,
                note = "Tăng sức đề kháng",
                disease = "Thiếu vitamin"
            )
        ),
        ReminderHistory(
            id = "rh5",
            label = "Nhắc uống khuya",
            date = "2025-05-09",
            time = "23:00",
            status = "Chưa xác nhận",
            reminder = PillReminder(
                id = "r5",
                label = "Uống thuốc ngủ",
                times = listOf(SelectedTime(5, "23:00")),
                frequency = FrequencyModel(5, "Khi cần thiết", true),
                medicines = listOf(
                    Medicine(id = "m5", name = "Melatonin", dosage = 1, unit = "viên")
                ),
                isOn = true,
                note = "Uống trước khi ngủ 30 phút",
                disease = "Mất ngủ"
            )
        )
    )

    val getHistoryList: LiveData<MutableList<ReminderHistory>> get() = _setHistoryList
    private val _setHistoryList = MutableLiveData<MutableList<ReminderHistory>>()

    fun getHistoryList() {
        _setHistoryList.value = sampleReminderHistories
    }

}