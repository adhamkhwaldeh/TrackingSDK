package com.kerberos.trackingSdk

import android.app.Application
import com.kerberos.livetrackingsdk.LiveTrackingManager
import com.kerberos.livetrackingsdk.di.KoinStarter

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LiveTrackingManager.initialize()
        KoinStarter.startKoin(this)
    }

}