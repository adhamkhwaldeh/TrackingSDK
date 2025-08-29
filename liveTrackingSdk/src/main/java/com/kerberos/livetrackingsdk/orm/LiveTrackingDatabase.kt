package com.kerberos.livetrackingsdk.orm

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Trip::class], version = 2)
abstract class LiveTrackingDatabase : RoomDatabase() {

    abstract fun vehiclePartsDao(): TripDao

}