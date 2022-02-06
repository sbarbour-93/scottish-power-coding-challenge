package com.scottbarbour.projects

import android.app.Application
import com.scottbarbour.projects.di.myKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AlbumsApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlbumsApp)
            modules(myKoinModules)
        }
    }
}