package com.example.medcare.views.health_advice.contact_doctor

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.medcare.base.BaseViewModel
import com.example.medcare.models.Doctor
import com.example.medcare.models.ReminderRelative

class ContactDoctorViewModel : BaseViewModel(){
    val doctors = listOf(
        Doctor(id = "1", name = "BS. Nguyễn Văn A", avatar = "https://example.com/avatar1.jpg"),
        Doctor(id = "2", name = "BS. Trần Thị B", avatar = "https://example.com/avatar2.jpg"),
        Doctor(id = "3", name = "BS. Lê Văn C", avatar = "https://example.com/avatar3.jpg"),
        Doctor(id = "4", name = "BS. Phạm Thị D", avatar = "https://example.com/avatar4.jpg"),
        Doctor(id = "5", name = "BS. Hồ Minh E", avatar = "https://example.com/avatar5.jpg")
    )
    val getDoctors: LiveData<MutableList<Doctor>> get() = _setDoctors
    private val _setDoctors = MutableLiveData<MutableList<Doctor>>()

    fun getDoctorList() {
        _setDoctors.value = doctors.toMutableList()
    }

}