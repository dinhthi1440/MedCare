package com.example.medcare.di

import com.example.medcare.data.datasource.auth.AuthDatasource
import com.example.medcare.data.datasource.auth.IAuthDatasource
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.data.datasource.medicine.MedicineDataSource
import com.example.medcare.data.datasource.pillreminder.IPillReminderDataSource
import com.example.medcare.data.datasource.pillreminder.PillReminderDataSource
import com.example.medcare.data.datasource.relatives.IRelativesDataSource
import com.example.medcare.data.datasource.relatives.RelativesDataSource
import com.example.medcare.data.repository.relatives.IRelativeRepos
import org.koin.dsl.module
val dataSourceModule = module {
    single<IMedicineDataSource> { MedicineDataSource(get()) }
    single<IPillReminderDataSource> { PillReminderDataSource(get()) }
    single<IAuthDatasource> { AuthDatasource() }
    single<IRelativesDataSource> { RelativesDataSource() }
}