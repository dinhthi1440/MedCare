package com.example.medcare

import android.app.Application
import com.example.medcare.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MeCareApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MeCareApplication)
            modules(
                viewModelModule
            )
        }
    }
}