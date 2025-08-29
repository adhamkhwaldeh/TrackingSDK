package com.kerberos.livetrackingsdk.repositories.paging.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kerberos.livetrackingsdk.models.TripModel
import com.kerberos.livetrackingsdk.orm.TripDao
import com.kerberos.livetrackingsdk.repositories.paging.PagingParamConfig

class TripPagingDataSource(
    private val tripDao: TripDao,
    private var payload: Int
) : PagingSource<Int, TripModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TripModel> {
        val pageNumber = params.key ?: PagingParamConfig.initialOffset
        return try {
            val tripEntities = tripDao.getTripsPaging()
            val result = tripEntities.load(
                LoadParams.Refresh(
                    key = pageNumber,
                    loadSize = params.loadSize,
                    placeholdersEnabled = false
                )
            )
            val data = if (result is LoadResult.Page) {
                result.data.map { trip ->
                    TripModel(
                        id = trip.id,
                        tripDuration = trip.tripDuration,
                        startTime = trip.startTime,
                        totalDistance = trip.totalDistance,
                        endTime = trip.endTime,
                        isActive = trip.isActive
                    )
                }
            } else {
                emptyList()
            }

            val nextPagePayload = params.key
            nextPagePayload?.inc()
//            result.load(params)

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = if (data.isEmpty()) null else nextPagePayload
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

//    override fun getRefreshKey(state: PagingState<Int, TripModel>): Int {
////        payload.set(PagingParamConfig.initialOffset)
//        payload = PagingParamConfig.initialOffset
//        return PagingParamConfig.initialOffset
//    }

    override fun getRefreshKey(state: PagingState<Int, TripModel>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the only page.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override val keyReuseSupported: Boolean
        get() = true//super.keyReuseSupported

}