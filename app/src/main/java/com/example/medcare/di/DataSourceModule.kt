package com.example.medcare.di

import com.example.medcare.data.datasource.auth.AuthDatasource
import com.example.medcare.data.datasource.auth.IAuthDatasource
import com.example.medcare.data.datasource.doctor.DoctorDatasource
import com.example.medcare.data.datasource.doctor.IDoctorDatasource
import com.example.medcare.data.datasource.feedback.FeedbackDatasource
import com.example.medcare.data.datasource.feedback.IFeedbackDatasource
import com.example.medcare.data.datasource.medicine.IMedicineDataSource
import com.example.medcare.data.datasource.medicine.MedicineDataSource
import com.example.medcare.data.datasource.pillreminder.IPillReminderDataSource
import com.example.medcare.data.datasource.pillreminder.PillReminderDataSource
import com.example.medcare.data.datasource.relatives.IRelativesDataSource
import com.example.medcare.data.datasource.relatives.RelativesDataSource
import com.example.medcare.data.datasource.reminder_history.IReminderHistoryDatasource
import com.example.medcare.data.datasource.reminder_history.ReminderHistoryDatasource
import com.example.medcare.data.repository.relatives.IRelativeRepos
import org.koin.dsl.module
val dataSourceModule = module {
    single<IMedicineDataSource> { MedicineDataSource(get()) }
    single<IPillReminderDataSource> { PillReminderDataSource(get()) }
    single<IAuthDatasource> { AuthDatasource() }
    single<IRelativesDataSource> { RelativesDataSource() }
    single<IReminderHistoryDatasource> { ReminderHistoryDatasource() }
    single<IDoctorDatasource> { DoctorDatasource() }
    single<IFeedbackDatasource> { FeedbackDatasource() }
}