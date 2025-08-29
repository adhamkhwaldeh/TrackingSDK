package com.kerberos.livetrackingsdk.orm

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface TripTrackDoa : BaseDao<TripTrack> {

    @Query("select * from triptrack")
    fun loadAllData(): List<TripTrack>

    @Query("SELECT * FROM trip ORDER BY id DESC")
    fun getTripsPaging(): PagingSource<Int, TripTrack>

    @Query("SELECT COUNT(*) FROM TripTrack where tripId=:tripId")
    fun tracksInTrip(tripId: Int): Int
}