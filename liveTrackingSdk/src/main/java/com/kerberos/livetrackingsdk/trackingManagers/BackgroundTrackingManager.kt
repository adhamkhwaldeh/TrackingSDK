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
import com.kerberos.livetrackingsdk.interfaces.ITrackingActionsInterface
import com.kerberos.livetrackingsdk.managers.LocationTrackingManager
import com.kerberos.livetrackingsdk.services.BaseTrackingService
import kotlinx.coroutines.DelicateCoroutinesApi
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
class BackgroundTrackingManager(
    context: Context,
    val serviceClass: Class<out BaseTrackingService>,
) : BaseTrackingManager(context) {

    var itsTrackService: ITrackingService? = null

    var locationTrackingManager: LocationTrackingManager? = null

    private val svcConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
            itsTrackService = ITrackingService.Stub.asInterface(binder)
            if (itsTrackService is IServiceExposeWithBinder) { // You'd need to know the concrete stub type
                locationTrackingManager =
                    (itsTrackService as IServiceExposeWithBinder).getLocationTrackingManager()

                Timber.d("")
                // Now you have the service instance, but this is only if in the same process
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            if (BuildConfig.DEBUG) {
                Log.d("PLAYER", "Service offline")
            }
            itsTrackService = null
            locationTrackingManager = null
        }
    }

    override val locationManager: LocationTrackingManager?
        get() {
            return locationTrackingManager
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

    //#region actions
    override fun onStartTracking(): Boolean {
        return try {
            itsTrackService?.startTracking() ?: false
        } catch (e: Exception) {
            return false
        }
    }

    override fun onResumeTracking(): Boolean {
        return try {
            itsTrackService?.pauseTracking() ?: false
        } catch (e: Exception) {
            return false
        }
    }

    override fun onPauseTracking(): Boolean {
        return try {
            itsTrackService?.pauseTracking() ?: false
        } catch (e: Exception) {
            return false
        }
    }

    override fun onStopTracking(): Boolean {
        return try {
            if (context != null) {
                try {
                    destroyTrackingManager()
                    val anIntent = Intent(context, serviceClass)
                    context?.stopService(anIntent)
                } catch (e: Exception) {

                }
            }
            itsTrackService?.stopTracking() ?: false
        } catch (e: Exception) {
            return false
        }
    }

    //#endregion

}