package com.aljawad.sons.gorestrepository.repositories

import androidx.paging.PagingData
import com.kerberos.livetrackingsdk.models.TripModel
import kotlinx.coroutines.flow.Flow

interface TripPagingRepository {
    fun getTripPageList(payload: Int): Flow<PagingData<TripModel>>
}