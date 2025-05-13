package com.example.medcare.di

import com.example.medcare.data.datasource.auth.AuthDatasource
import com.example.medcare.data.datasource.auth.IAuthDatasource
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.data.datasource.medicine.MedicineDataSource
import com.example.medcare.data.datasource.pillreminder.IPillReminderDataSource
import com.example.medcare.data.datasource.pillreminder.PillReminderDataSource
import org.koin.dsl.module
val dataSourceModule = module {
    single<IMedicineDataSource.Local> { MedicineDataSource(get()) }
    single<IPillReminderDataSource.Local> { PillReminderDataSource(get()) }
    single<IAuthDatasource.Remote> { AuthDatasource() }
}