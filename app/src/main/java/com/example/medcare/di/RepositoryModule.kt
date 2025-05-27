package com.example.medcare.di

import com.example.medcare.data.repository.auth.AuthRepository
import com.example.medcare.data.repository.auth.IAuthRepository
import com.example.medcare.data.repository.doctor.DoctorRepos
import com.example.medcare.data.repository.doctor.IDoctorRepos
import com.example.medcare.data.repository.feedback.FeedbackRepository
import com.example.medcare.data.repository.feedback.IFeedbackRepository
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.data.repository.medicine.MedicineRepos
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.data.repository.pillreminder.PillReminderRepos
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.data.repository.relatives.RelativeRepos
import com.example.medcare.data.repository.reminder_history.IReminderHistoryRepos
import com.example.medcare.data.repository.reminder_history.ReminderHistoryRepos
import org.koin.dsl.module

val repositoryModule = module {
    single<IMedicineRepos> { MedicineRepos(get()) }
    single<IPillReminderRepos> { PillReminderRepos(get()) }
    single<IAuthRepository> { AuthRepository(get()) }
    single<IRelativeRepos> { RelativeRepos(get()) }
    single<IReminderHistoryRepos> { ReminderHistoryRepos(get()) }
    single<IDoctorRepos> { DoctorRepos(get()) }
    single<IFeedbackRepository> { FeedbackRepository(get()) }
}