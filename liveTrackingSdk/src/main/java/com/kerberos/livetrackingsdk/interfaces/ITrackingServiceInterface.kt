package com.kerberos.livetrackingsdk.interfaces

import android.app.Notification
import android.app.Service
import android.os.Build
import androidx.annotation.RequiresApi
import com.kerberos.livetrackingsdk.models.DefaultNotificationConfiguration

interface ITrackingServiceInterface : ITrackingActionsInterface, ITrackingLocationInterface {

    val serviceClassForRestart: Class<out Service>
        get


    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotification(): Notification

    val defaultNotificationConfiguration: DefaultNotificationConfiguration?
        get() {
            return null
        }

}