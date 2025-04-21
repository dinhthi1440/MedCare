package com.example.medcare.di

import com.example.medcare.data.repository.medicine.IMedicineRepos
import com.example.medcare.data.repository.medicine.MedicineRepos
import com.example.medcare.data.repository.pillreminder.IPillReminderRepos
import com.example.medcare.data.repository.pillreminder.PillReminderRepos
import org.koin.dsl.module

val repositoryModule = module {
    single<IMedicineRepos.Local> { MedicineRepos(get()) }
    single<IPillReminderRepos.Local> { PillReminderRepos(get()) }
}