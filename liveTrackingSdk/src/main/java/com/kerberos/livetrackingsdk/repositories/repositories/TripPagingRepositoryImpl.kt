package com.kerberos.livetrackingsdk.repositories.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData

import com.aljawad.sons.gorestrepository.repositories.TripPagingRepository
import com.kerberos.livetrackingsdk.models.TripModel
import com.kerberos.livetrackingsdk.orm.TripDao
import com.kerberos.livetrackingsdk.repositories.paging.PagingParamConfig
import com.kerberos.livetrackingsdk.repositories.paging.dataSource.TripPagingDataSource
import kotlinx.coroutines.flow.Flow

class TripPagingRepositoryImpl constructor(private val tripDao: TripDao) :
    TripPagingRepository {


    @ExperimentalPagingApi
    override fun getTripPageList(payload: Int): Flow<PagingData<TripModel>> = Pager(
        config = PagingConfig(
            pageSize = PagingParamConfig.pageSize,
            prefetchDistance = PagingParamConfig.prefetchDistance,
            enablePlaceholders = PagingParamConfig.enablePlaceholders,
            initialLoadSize = PagingParamConfig.initialLoadSize,
        ),
        pagingSourceFactory = { TripPagingDataSource(tripDao, payload) },
        initialKey = payload,
        remoteMediator = null

    ).flow

}