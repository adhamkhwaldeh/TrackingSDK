package com.kerberos.trackingSdk.orm

import androidx.room.Dao
import androidx.room.Query
import androidx.paging.PagingSource

@Dao
interface TripDao : BaseDao<Trip> {
    @Query("select * from trip")
    fun loadAllData(): List<Trip>

    @Query("SELECT * FROM trip ORDER BY id DESC")
    fun getTripsPaging(): PagingSource<Int, Trip>
}