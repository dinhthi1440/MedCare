package com.example.medcare.views.connect_relatives.relative_request_add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.models.Medicine
import com.example.medcare.models.PillReminder
import com.example.medcare.models.ReminderRelative
import com.example.medcare.views.pill_reminder.add_new_reminder.add_frequency.FrequencyModel
import com.example.medcare.views.pill_reminder.add_new_reminder.model.SelectedTime

class RelativeRequestViewModel(private val iRelativeRepos: IRelativeRepos) : BaseViewModel() {
    val reminderTo = mutableListOf(
        ReminderRelative(
            "id1",
            "user_123",
            "Alice",
            "avatar1.png",
            "Tôi",
            "user_a",
            "Bob",
            "avatarA.png", "Bạn bè",
            PillReminder(
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
        ReminderRelative(
            "id2",
            "user_123",
            "Alice",
            "avatar1.png",
            "Tôi",
            "user_b",
            "Charlie",
            "avatarB.png", "Bạn bè",
            PillReminder(
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
        ReminderRelative(
            "id3",
            "user_123",
            "Alice",
            "avatar1.png",
            "Tôi",
            "user_c",
            "David",
            "avatarC.png", "Bạn bè",
            PillReminder(
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
        ReminderRelative(
            "id4", "user_123", "Alice", "avatar1.png", "Tôi",
            "user_d", "Eva", "avatarD.png", "Bạn bè",
            PillReminder(
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
        ReminderRelative(
            "id5",
            "user_123",
            "Alice",
            "avatar1.png",
            "Tôi",
            "user_e",
            "Fiona",
            "avatarE.png", "Bạn bè",
            PillReminder(
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

        )
    val reminderFrom = mutableListOf(
        ReminderRelative(
            "id6",
            "user_x",
            "George",
            "avatarX.png", "Bạn bè",
            "user_456",
            "Alice",
            "avatar1.png",
            "Tôi",
            PillReminder(
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
        ReminderRelative(
            "id7",
            "user_y",
            "Harry",
            "avatarY.png",
            "user_456", "Bạn bè",
            "Alice",
            "avatar1.png",
            "Tôi",
            PillReminder(
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
        ReminderRelative(
            "id8", "user_z", "Ivy", "avatarZ.png",  "Bạn bè",
            "user_456", "Alice", "avatar1.png", "Tôi",
            PillReminder(
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
        ReminderRelative(
            "id9",
            "user_m",
            "Jack",
            "avatarM.png",
            "user_456", "Bạn bè",
            "Alice",
            "avatar1.png",
            "Tôi",
            PillReminder(
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
        ReminderRelative(
            "id10",
            "user_n",
            "Kate",
            "avatarN.png",
            "user_456",
            "Bạn bè",
            "Alice",
            "avatar1.png",
            "Tôi",
            PillReminder(
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
        )
    )

    val getReminderFrom: LiveData<MutableList<ReminderRelative>> get() = _setReminderFrom
    private val _setReminderFrom = MutableLiveData<MutableList<ReminderRelative>>()
    val getReminderTo: LiveData<MutableList<ReminderRelative>> get() = _setReminderTo
    private val _setReminderTo = MutableLiveData<MutableList<ReminderRelative>>()

    fun getData() {
        _setReminderFrom.value = reminderFrom
        _setReminderTo.value = reminderTo
    }

}