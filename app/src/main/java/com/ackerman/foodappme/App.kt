package com.ackerman.foodappme

import android.app.Application
import com.ackerman.foodappme.data.local.database.AppDatabase
import com.ackerman.foodappme.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(AppModules.modules)
        }
    }
}
