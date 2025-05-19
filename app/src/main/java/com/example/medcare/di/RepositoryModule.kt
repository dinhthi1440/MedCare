package com.example.medcare.di

import com.example.medcare.data.repository.auth.AuthRepository
import com.example.medcare.data.repository.auth.IAuthRepository
import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.data.repository.medicine.MedicineRepos
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.data.repository.pillreminder.PillReminderRepos
import com.example.medcare.data.repository.relatives.IRelativeRepos
import com.example.medcare.data.repository.relatives.RelativeRepos
import org.koin.dsl.module

val repositoryModule = module {
    single<IMedicineRepos> { MedicineRepos(get()) }
    single<IPillReminderRepos> { PillReminderRepos(get()) }
    single<IAuthRepository> { AuthRepository(get()) }
    single<IRelativeRepos> { RelativeRepos(get()) }
}