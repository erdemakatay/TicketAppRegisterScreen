package com.turkcell.ticketapp

import android.app.Application
import com.turkcell.data.di.dataModule
import com.turkcell.ticketapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


// Uygulama başladığında activitylerden önce oluşturulur.
// Singleton (uygulama yaşadığı sürece , tek bir instance olarak memoryde kalır)
// Uygulama kapanana kadar yok edilmez...
class TicketAppAplication : Application() {
    override fun onCreate () {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TicketAppAplication)
            modules(
                dataModule, // Data module olarak tanımlanan bağımlılıkları projende aktif et.
                appModule,
            )
        }
    }
}