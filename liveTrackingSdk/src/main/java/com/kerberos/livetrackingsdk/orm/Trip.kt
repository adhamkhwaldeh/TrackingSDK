package com.kerberos.livetrackingsdk.orm

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
class Trip {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "startTime")
    var startTime: Long = 0

    @ColumnInfo(name = "endTime")
    var endTime: Long? = null

    @ColumnInfo(name = "tripDuration")
    var tripDuration: Long? = null

    @ColumnInfo(name = "totalDistance")
    var totalDistance: Double? = null

    @ColumnInfo(name = "isActive")
    var isActive: Boolean = false
}
