package com.kerberos.trackingSdk.orm

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
class TripTrack {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "tripId")
    var tripId: Int = 0

    @ColumnInfo(name = "latitude")
    var latitude: Double = 0.0

    @ColumnInfo(name = "longitude")
    var longitude: Double = 0.0

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0

    @ColumnInfo(name = "speed")
    var speed: Double = 0.0
}
