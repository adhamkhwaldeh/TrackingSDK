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
import com.kerberos.livetrackingsdk.interfaces.ITrackingManagerInterface
import com.kerberos.livetrackingsdk.models.TrackSDKConfigurationModel
import com.kerberos.livetrackingsdk.services.BaseTrackingService
import kotlinx.coroutines.DelicateCoroutinesApi
import timber.log.Timber

@OptIn(DelicateCoroutinesApi::class)
class BackgroundTrackingManager(
    val serviceClass: Class<out BaseTrackingService>,
) : ITrackingManagerInterface {

    var mainContext: Context? = null

    var itsTrackService: ITrackingService? = null

    var serviceInstance: BaseTrackingService? = null

    private val svcConn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, binder: IBinder) {
//            if (BuildConfig.DEBUG) {
//                Log.d("PLAYER", "Service came online");
//            }
            itsTrackService = ITrackingService.Stub.asInterface(binder)
            if (itsTrackService is IServiceExposeWithBinder) { // You'd need to know the concrete stub type
                serviceInstance =
                    (itsTrackService as IServiceExposeWithBinder).getServiceInstance()
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

    fun bind(context: Context) {
        val anIntent = Intent(context, serviceClass)
        mainContext = context
        context.bindService(anIntent, svcConn, Context.BIND_AUTO_CREATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, anIntent)
        } else {
            context.startService(anIntent)
        }
    }

    fun unBind(context: Context) {
        try {
            context.unbindService(svcConn)
        } catch (e: java.lang.Exception) {
        }
    }


//    override val sdkConfiguration: TrackSDKConfigurationModel
//        get() {
//            return if (serviceInstance != null) {
//                serviceInstance!!.ma
//            } else {
//                TrackSDKConfigurationModel(
//                    locationUpdateInterval = 0L,
//                    backgroundTrackingToggle = false,
//                    serviceClass = null
//                )
//            }
//        }

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
            if (mainContext != null) {
                try {
                    unBind(mainContext!!)
                    val anIntent = Intent(mainContext, serviceClass)
                    mainContext?.stopService(anIntent)
                } catch (e: Exception) {

                }
            }
            itsTrackService?.stopTracking() ?: false
        } catch (e: Exception) {
            return false
        }
    }


}