package com.kerberos.livetrackingsdk.backgroundTracking

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.RemoteException
import android.os.SystemClock
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.kerberos.livetrackingsdk.ITrackingService
import com.kerberos.livetrackingsdk.R
import com.kerberos.livetrackingsdk.useCases.AddCurrentTripTrackUseCase
//import com.tatweer.common.extensions.requestBlocking
//import com.tatweer.unifiedinspection.HomeActivity
//import com.tatweer.unifiedinspection.data.source.remote.RetrofitCoreBuilder
//import com.tatweer.unifiedinspection.dtos.data.area.models.InspectorTrackingRequest
//import com.tatweer.unifiedinspection.dtos.data.area.models.LocationPointData
//import com.tatweer.unifiedinspection.dtos.helpers.ConstantHelper
//import com.tatweer.unifiedinspection.dtos.helpers.ConstantHelper.DEVICE_STATUS_ACTION
//import com.tatweer.unifiedinspection.dtos.helpers.ConstantHelper.INSPECTOR_LAST_UPDATE_DATE_KEY
//import com.tatweer.unifiedinspection.dtos.helpers.ConstantHelper.INSPECTOR_STATUS_KEY
//import com.tatweer.unifiedinspection.dtos.helpers.ConstantHelper.TRACKING_STATUS_ACTION
//import com.tatweer.unifiedinspection.features.inspectorTracking.InspectionTrackingApiService
//import com.tatweer.unifiedinspection.utils.NetworkConnectionDetector
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.sql.Timestamp
import kotlin.concurrent.thread

//TODO need to be checked @RequiresApi(Build.VERSION_CODES.O)

@DelicateCoroutinesApi
class TrackingService : Service() {

    private var wakeLock: PowerManager.WakeLock? = null
    private val failPoints = mutableListOf<String>()
    private val gpsIteratorCall = mutableListOf<String>()
    private var isGpsEnabled = true
    private var isNetworkEnabled = true
    private var isNetworkEnabledForLiveTracking = true

    //    private val addCurrentTripTrackUseCase: AddCurrentTripTrackUseCase by inject()
//
//
    private val itsBinder: ITrackingService.Stub = object : ITrackingService.Stub() {
        @Throws(RemoteException::class)
        override fun startTracking(): Boolean {
            TODO("Not yet implemented")
        }


        @Throws(RemoteException::class)
        override fun pauseTracking(): Boolean {
            TODO("Not yet implemented")
        }

        @Throws(RemoteException::class)
        override fun resumeTracking(): Boolean {
            TODO("Not yet implemented")
        }

        @Throws(RemoteException::class)
        override fun stopTracking(): Boolean {
            TODO("Not yet implemented")
        }

//        @Throws(RemoteException::class)
//        fun Play(theUrl: String?, theName: String?, theID: String?, isAlarm: Boolean) {
//            this@PlayerService.playUrl(CanonicalAudioEntity(theID, -1, theUrl, theName), isAlarm)
//        }
//        @Throws(RemoteException::class)
//        fun Pause() {
//            this@PlayerService.pause()
//        }

//        @get:Throws(RemoteException::class)
//        val timerSeconds: Long
//            get() = this@PlayerService.getTimerSeconds()
//
//        @get:Throws(RemoteException::class)
//        val currentStationID: String
//            get() = audioEntity.getId()
//
//        @get:Throws(RemoteException::class)
//        val stationName: String
//            get() = audioEntity.getStringName(itsContext)
//
//        @get:Throws(RemoteException::class)
//        val metadataLive: StreamLiveInfo
//            get() = this@PlayerService.liveInfo

        //        @get:Throws(RemoteException::class)
//        val metadataStreamName: String?
//            get() {
//                if (streamInfo != null) return streamInfo.audioName
//                return null
//            }


    }


    private val isTimeInRange: Boolean
        get() {
            return true
        }

    override fun onBind(intent: Intent): IBinder? {
        Timber.d("Some component want to bind with the service")
        // We don't provide binding, so return null
        return itsBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("onStartCommand executed with startId: $startId")
        //TODO wee need them to handle ADEL service
//        if (intent != null) {
//            val action = intent.action
//            Timber.d("using an intent with action $action")
//            Timber.d("service_time_range==${isTimeInRange}")
//            when (action) {
//                Actions.START.name -> startTrackingService()
//                Actions.STOP.name -> stopService()
//                else -> Timber.d("This should never happen. No action in the received intent")
//            }
//        } else {
//            log(
//                "with a null intent. It has been probably restarted by the system."
//            )
//        }
        // by returning this we make sure the service is restarted if the system kills the service


        //TODO need to check if we enabled background services from the configuration
        sendCurrentLocation()

        return START_STICKY
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WakelockTimeout")
    override fun onCreate() {
        super.onCreate()

        Timber.d("The service has been created")
        //TODO need to be revised

//        startForeground(1, createNotification())

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK,
                    "ContinuousInspectorTracking::lock"
                ).apply {
//                    acquire(10 * 60 * 1000L /*10 minutes*/)
                    acquire()
                }
            }

        startTrackingService()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("The service has been destroyed")
//        wakeLock?.release();
    }

    private fun startTrackingService() {
        //TODO first point
        Timber.d("Starting the foreground service task")
//        Toast.makeText(this, this.getString(R.string.follow_up_inspector_msg), Toast.LENGTH_SHORT)
//            .show()

        handleGeneralTracking()


        handleGpsAndNetworkConnection()
    }

    private fun handleGeneralTracking() {
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                repeat(12 * 10) { index ->
//                repeat(5) { index ->
                    if (isTimeInRange) {
                        Timber.d("service_time_range==${isTimeInRange}")
                        getCurrentLocation()
                    } else {
                        failPoints.clear()
                        gpsIteratorCall.clear()
                    }
                    delay(1 * 5 * 1000)
                }
                if (isTimeInRange) {
                    sendCurrentLocation()
                } else {
                    failPoints.clear()
                    gpsIteratorCall.clear()
                }
            }

        }
    }

    private fun onLocationFetchedProperly(currentLocation: Location) {
//        if (pointsList.isEmpty()) {
//            pointsList.add(
//                LocationPointData(
//                    latitude = currentLocation.latitude,
//                    longitude = currentLocation.longitude,
//                    timeStamp = Timestamp(System.currentTimeMillis()).toString()
//                )
//            )
//        } else {
//            val distance = LocationDistanceHelper.distanceMeter(
//                currentLocation.latitude,
//                currentLocation.longitude,
//                pointsList.lastOrNull()?.latitude ?: 0.0,
//                pointsList.lastOrNull()?.longitude ?: 0.0,
//            )
//            Timber.d("service_location==${distance}")
//            if (!(distance <= ConstantHelper.MIN_DISTANCE) && !distance.isNaN()) {
//                pointsList.add(
//                    LocationPointData(
//                        latitude = currentLocation.latitude,
//                        longitude = currentLocation.longitude,
//                        timeStamp = Timestamp(System.currentTimeMillis()).toString()
//                    )
//                )
//            } else {
//                excludedPoints.add(
//                    LocationPointData(
//                        latitude = currentLocation.latitude,
//                        longitude = currentLocation.longitude,
//                        timeStamp = Timestamp(System.currentTimeMillis()).toString()
//                    )
//                )
//            }
//        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        try {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsValue = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val requestedTimeStamp = Timestamp(System.currentTimeMillis()).toString()
            gpsIteratorCall.add(requestedTimeStamp)
            LocationTrackingHelper.updateLocation(
                this@TrackingService,
                locationCallback = {
                    it?.let { currentLocation ->
                        onLocationFetchedProperly(currentLocation)
                    }
                },
                failCallback = {
                    if (it == "6: RESOLUTION_REQUIRED") {
                        if (gpsValue) {
                            getCurrentLocationWithDexter()
                        }
                    } else {
                        failPoints.add("message=$it -- time=${Timestamp(System.currentTimeMillis())}")
                    }
                },
            )
        } catch (ex: Exception) {
            failPoints.add("message=${ex.message} -- time=${Timestamp(System.currentTimeMillis())}")
        }
    }

    private fun getCurrentLocationWithDexter() {
        try {
            val requestedTimeStamp = Timestamp(System.currentTimeMillis()).toString()
            gpsIteratorCall.add(requestedTimeStamp)
            //TODO need to be revised
//            LocationTrackingHelper.updateLocationWithDexter(
//                this@TrackingService,
//                locationCallback = {
//                    it?.let { currentLocation ->
//                        onLocationFetchedProperly(currentLocation)
//                    }
//                },
//                failCallback = {
//                    failPoints.add("message=$it -- time=${Timestamp(System.currentTimeMillis())}")
//                },
//            )
        } catch (ex: Exception) {
            failPoints.add("message=${ex.message} -- time=${Timestamp(System.currentTimeMillis())}")
        }
    }

//    private fun stopService() {
//        Timber.d("Stopping the foreground service")
//        Toast.makeText(this, R.string.stopping_follow_inspection, Toast.LENGTH_SHORT).show()
//        try {
//            wakeLock?.let {
//                if (it.isHeld) {
//                    it.release()
//                }
//            }
//            stopForeground(STOP_FOREGROUND_DETACH)
//            stopSelf()
//        } catch (e: Exception) {
//            Timber.d("Service stopped without being started: ${e.message}")
//        }
////        isServiceStarted = false
//    }

    @SuppressLint("HardwareIds")
    private fun sendCurrentLocation() {
        //TODO need to add the data locally

//        val request = InspectorTrackingRequest(
//            inspectorId = appUserManager.getUserID(),
//            inspectorName = appUserManager.fullName,
//            deviceId = Settings.Secure.getString(
//                this.contentResolver,
//                Settings.Secure.ANDROID_ID
//            ),
//            points = pointsList,
////            failPoints = failPoints,
////            excludedPoints = excludedPoints
//        )
//
//        Timber.d("service_location_request==${Gson().toJson(request)}")
//        thread(start = true) {
//
//
//            val retrofit = RetrofitCoreBuilder.retrofitBuilder(appUserManager)
//            val inspectionTrackingApi =
//                retrofit.create(InspectionTrackingApiService::class.java)
//            val response = inspectionTrackingApi.addInspectionLocationPoints(
//                request,
//            ).requestBlocking()
//            response.either(
//                {
//                    Timber.d("api_error==${it.message}")
//                    fireMissedPointsBroadCastReceiver()
//                },
//                {
//                    Timber.d("service_location_request==${"success"}")
//                    if (pointsList.isNotEmpty()) {
//                        fireMissedPointsBroadCastReceiver(
//                            Timestamp(
//                                System.currentTimeMillis()
//                            ).toString()
//                        )
//                        appUserManager.saveLastSyncDataTime(
//                            Timestamp(
//                                System.currentTimeMillis()
//                            ).toString()
//                        )
//                    }
//                },
//            )
//        }

    }

    private fun fireMissedPointsBroadCastReceiver(lastUpdateDate: String = "") {
        //TODO need to check failure and callback
//        val intent = Intent()
//        intent.putExtra(INSPECTOR_LAST_UPDATE_DATE_KEY, lastUpdateDate)
//        intent.action = TRACKING_STATUS_ACTION
//        intent.setPackage(this@TrackingService.packageName)
//        sendBroadcast(intent)
    }

    private fun handleGpsAndNetworkConnection() {
        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                updateMobileStatus()
            }
        }
    }

    private fun updateMobileStatus() {
        //TODO check network and gps status
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsValue = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        val isConnected = NetworkConnectionDetector.isConnected
////        Timber.d("is_Gps_enabled==${isGpsEnabled}")
////        Timber.d("is_Network_enabled==${isConnected}")
//        if (isGpsEnabled != gpsValue || isNetworkEnabled != isConnected) {
//            thread(start = true) {
//                val retrofit = RetrofitCoreBuilder.retrofitBuilder(appUserManager)
//                val inspectionTrackingApi =
//                    retrofit.create(InspectionTrackingApiService::class.java)
//                val response =
//                    inspectionTrackingApi.updateMobileStatus(isGpsEnabled, isNetworkEnabled)
//                        .requestBlocking()
//                response.either(
//                    {
//                        fireInspectorStatusBroadCastReceiver(false)
//                    },
//                    {
//                        fireInspectorStatusBroadCastReceiver(isGpsEnabled && isNetworkEnabled)
//                    },
//                )
//            }
//        }

        isGpsEnabled = gpsValue
//        isNetworkEnabled = isConnected
    }


    //TODO need to be uncommitted createNotification
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotification(): Notification {
//        val notificationChannelId = "ENDLESS SERVICE CHANNEL"
//
//        // depending on the Android API that we're dealing with we will have
//        // to use a specific method to create the notification
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val channel = NotificationChannel(
//            notificationChannelId,
//            "Endless Service notifications channel",
//            NotificationManager.IMPORTANCE_HIGH
//        ).let {
//            it.description = "Endless Service channel"
//            it.enableLights(true)
//            it.lightColor = Color.RED
//            it.enableVibration(true)
//            it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
//            it
//        }
//        notificationManager.createNotificationChannel(channel)
//
//        //TODO the block below need to be uncommitted
//        val pendingIntent: PendingIntent =
//            Intent(this, HomeActivity::class.java).let { notificationIntent ->
//
////                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    PendingIntent.getActivity(
//                        this,
//                        0,
//                        notificationIntent,
//                        PendingIntent.FLAG_MUTABLE
//                    )
//                } else {
//                    PendingIntent.getActivity(
//                        this,
//                        0,
//                        notificationIntent,
//                        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//                    )
//                }
//            }
//
//        val builder: Notification.Builder =
//            Notification.Builder(
//                this,
//                notificationChannelId
//            )
//
//        return builder
//            .setContentTitle(this.getString(R.string.follow_up_inspector_title))
//            .setContentText(this.getString(R.string.follow_up_inspector_msg))
//            .setContentIntent(pendingIntent)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setTicker("Ticker text")
//            .build()
//    }


    override fun onTaskRemoved(rootIntent: Intent) {
        val context = this@TrackingService
        val restartServiceIntent =
            Intent(context, TrackingService::class.java).also {
                it.setPackage(packageName)
            }
        val restartServicePendingIntent: PendingIntent =
            PendingIntent.getService(
                this, 1, restartServiceIntent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )
        context.getSystemService(Context.ALARM_SERVICE)
        val alarmService: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000,
            restartServicePendingIntent
        )

//        val restartServiceIntent =
//            Intent(applicationContext, ContinuousInspectorTracking::class.java).also {
//                it.setPackage(packageName)
//            };
//        val restartServicePendingIntent: PendingIntent =
//            PendingIntent.getService(
//                this, 1, restartServiceIntent,
//                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
//            )
//        applicationContext.getSystemService(Context.ALARM_SERVICE);
//        val alarmService: AlarmManager =
//            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
//        alarmService.set(
//            AlarmManager.ELAPSED_REALTIME,
//            SystemClock.elapsedRealtime() + 1000,
//            restartServicePendingIntent
//        )
    }

}