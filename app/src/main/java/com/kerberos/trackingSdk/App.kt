package com.kerberos.trackingSdk

import android.app.Application
import com.kerberos.livetrackingsdk.LiveTrackingManager;

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        LiveTrackingManager.initialize()
    }

}