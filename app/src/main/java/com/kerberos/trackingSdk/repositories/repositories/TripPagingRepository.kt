package com.kerberos.trackingSdk.repositories.repositories

import androidx.paging.PagingData
import com.kerberos.trackingSdk.models.TripModel
import kotlinx.coroutines.flow.Flow

interface TripPagingRepository {
    fun getTripPageList(payload: Int): Flow<PagingData<com.kerberos.trackingSdk.models.TripModel>>
}