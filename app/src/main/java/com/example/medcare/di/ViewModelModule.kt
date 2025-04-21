package com.example.medcare.di

import com.example.medcare.views.add_new_reminder.NewReminderViewModel
import com.example.medcare.views.add_new_reminder.select_medicines.SelectMedicineViewModel
import com.example.medcare.views.home.HomeViewModel
import com.example.medcare.views.login.AuthViewModel
import com.example.medcare.views.medication_reminder.MedicationReminderViewModel
import com.example.medcare.views.my_medicine.add_medicine.AddMedicineViewModel
import com.example.medcare.views.my_medicine.medicine_detail.MedicineDetailViewModel
import com.example.medcare.views.my_medicine.medicine_list.MyMedicineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel() }
    viewModel { HomeViewModel() }
    viewModel { MyMedicineViewModel(get()) }
    viewModel { MedicineDetailViewModel() }
    viewModel { AddMedicineViewModel(get()) }
    viewModel { MedicationReminderViewModel() }
    viewModel { NewReminderViewModel(get()) }
    viewModel { SelectMedicineViewModel(get()) }
}