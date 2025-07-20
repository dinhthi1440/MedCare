package com.example.medcare.di

import android.content.Context
import androidx.room.Room
import com.example.medcare.data.database.local.DataBaseLocal
import org.koin.dsl.module

val databaseModule = module {
    single { provideLocalDatabase(get()) }
    single { provideMedicineDao(get()) }
    single { providePillReminderDao(get()) }
    single { provideHistoryDao(get()) }
}
private fun provideLocalDatabase(context: Context): DataBaseLocal {
    return Room.databaseBuilder(
        context.applicationContext,
        DataBaseLocal::class.java,
        DataBaseLocal.NAME
    ).build()
}

private fun provideMedicineDao(local: DataBaseLocal) = local.medicineDao
private fun providePillReminderDao(local: DataBaseLocal) = local.pillReminderDAO
private fun provideHistoryDao(local: DataBaseLocal) = local.historyDAO