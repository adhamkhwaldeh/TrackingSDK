package com.kerberos.livetrackingsdk.orm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Trip::class], version = 2)
abstract class LiveTrackingDatabase : RoomDatabase() {

    abstract fun tripDao(): TripDao

    abstract fun tripTrackDao(): TripDao

    companion object {
        @Volatile
        private var INSTANCE: LiveTrackingDatabase? = null

        fun getDatabase(context: Context): LiveTrackingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LiveTrackingDatabase::class.java,
                    "live_tracking_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

