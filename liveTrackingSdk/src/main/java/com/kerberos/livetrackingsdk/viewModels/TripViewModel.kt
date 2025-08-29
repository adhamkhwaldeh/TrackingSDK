package com.kerberos.livetrackingsdk.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.aljawad.sons.gorestrepository.repositories.TripPagingRepository
import com.kerberos.livetrackingsdk.models.TripModel
import kotlinx.coroutines.flow.Flow

class TripViewModel constructor(private val tripPagingRepository: TripPagingRepository) :
    ViewModel() {

    fun getTripList(): Flow<PagingData<TripModel>> {
        return tripPagingRepository.getTripPageList(1).cachedIn(viewModelScope)
    }

}