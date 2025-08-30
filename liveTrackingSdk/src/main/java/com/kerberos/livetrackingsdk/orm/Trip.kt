package com.kerberos.livetrackingsdk.orm

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Keep
@Entity
class Trip() {
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

    @Ignore
//    @JvmOverloads
    constructor(
        id: Int = 0,
        startTime: Long = 0,
        endTime: Long? = null,
        tripDuration: Long? = null,
        totalDistance: Double? = null,
        isActive: Boolean = false
    ) : this() {
        this.id = id
        this.startTime = startTime
        this.endTime = endTime
        this.tripDuration = tripDuration
        this.totalDistance = totalDistance
        this.isActive = isActive
    }
}
