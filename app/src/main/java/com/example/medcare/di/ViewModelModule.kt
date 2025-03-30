package com.example.medcare.di

import com.example.medcare.views.home.HomeViewModel
import com.example.medcare.views.login.AuthViewModel
import com.example.medcare.views.my_medicine.medicine_detail.MedicineDetailViewModel
import com.example.medcare.views.my_medicine.medicine_list.MyMedicineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel() }
    viewModel { HomeViewModel() }
    viewModel { MyMedicineViewModel() }
    viewModel { MedicineDetailViewModel() }
}