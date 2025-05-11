package com.example.medcare.di

import com.example.medcare.views.connect_relatives.connect_relatives_list.ConnectRelativesViewModel
import com.example.medcare.views.connect_relatives.relative_reminder_detail.RelativeReminderDetailViewModel
import com.example.medcare.views.connect_relatives.relative_reminder_history.RelativeHistoryViewModel
import com.example.medcare.views.connect_relatives.relative_request_add.RelativeRequestViewModel
import com.example.medcare.views.pill_reminder.add_new_reminder.NewReminderViewModel
import com.example.medcare.views.pill_reminder.add_new_reminder.select_medicines.SelectMedicineViewModel
import com.example.medcare.views.home.HomeViewModel
import com.example.medcare.views.login.AuthViewModel
import com.example.medcare.views.pill_reminder.MedicationReminderViewModel
import com.example.medcare.views.my_medicine.add_medicine.AddMedicineViewModel
import com.example.medcare.views.my_medicine.medicine_detail.MedicineDetailViewModel
import com.example.medcare.views.my_medicine.medicine_list.MyMedicineViewModel
import com.example.medcare.views.pill_reminder.reminder_detail.ReminderDetailViewModel
import com.example.medcare.views.reminder_history.reminder_history_list.ReminderHistoryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel() }
    viewModel { HomeViewModel() }
    viewModel { MyMedicineViewModel(get()) }
    viewModel { MedicineDetailViewModel(get()) }
    viewModel { AddMedicineViewModel(get()) }
    viewModel { MedicationReminderViewModel(get()) }
    viewModel { NewReminderViewModel(get()) }
    viewModel { SelectMedicineViewModel(get()) }
    viewModel { ReminderDetailViewModel(get()) }
    viewModel { ReminderHistoryViewModel() }
    viewModel { ConnectRelativesViewModel() }
    viewModel { RelativeHistoryViewModel() }
    viewModel { RelativeRequestViewModel() }
    viewModel { RelativeReminderDetailViewModel() }
}