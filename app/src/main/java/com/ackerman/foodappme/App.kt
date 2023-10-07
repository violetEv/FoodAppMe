package com.ackerman.foodappme

import android.app.Application
import com.ackerman.foodappme.data.local.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.getInstance(this)
    }
}