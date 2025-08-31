package com.kerberos.livetrackingsdk.trackingManagers

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import com.kerberos.livetrackingsdk.BuildConfig
import com.kerberos.livetrackingsdk.ITrackingService
import com.kerberos.livetrackingsdk.interfaces.IServiceExposeWithBinder
import com.kerberos.livetrackingsdk.interfaces.ITrackingSdkModeStatusListener
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager
import com.kerberos.livetrackingsdk.services.BaseTrackingService
import kotlinx.coroutines.DelicateCoroutinesApi
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
class BackgroundTrackingManager(
    context: Context,
    val serviceClass: Class<out BaseTrackingService>,
    trackingSdkModeStatusListener: ITrackingSdkModeStatusListener
) : BaseTrackingManager(context, trackingSdkModeStatusListener) {

    var itsTrackService: ITrackingService? = null

    private val svcConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            itsTrackService = ITrackingService.Stub.asInterface(binder)
            if (itsTrackService is IServiceExposeWithBinder) { // You'd need to know the concrete stub type

                val locationTrackingManager =
                    (itsTrackService as IServiceExposeWithBinder).getLocationTrackingManager()

                trackingSdkModeStatusListener.onTrackingSDKModeInitialized(
                    locationTrackingManager!!,
                    com.kerberos.livetrackingsdk.enums.LiveTrackingMode.BACKGROUND_SERVICE
                )
                Timber.d("")
                // Now you have the service instance, but this is only if in the same process
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            if (BuildConfig.DEBUG) {
                Log.d("PLAYER", "Service offline")
            }
            itsTrackService = null
        }
    }

    //#region SDK actions

    override fun initializeTrackingManager(): Boolean {
        val anIntent = Intent(context, serviceClass)
        context.bindService(anIntent, svcConn, Context.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, anIntent)
        } else {
            context.startService(anIntent)
        }
        return true
    }

    override fun destroyTrackingManager(): Boolean {
        try {
            context.unbindService(svcConn)
        } catch (e: java.lang.Exception) {

        }
        return true
    }
    //#endregion


}