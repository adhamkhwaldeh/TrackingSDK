package com.kerberos.trackingSdk.repositories.paging.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kerberos.trackingSdk.models.TripModel
import com.kerberos.trackingSdk.orm.TripDao
import com.kerberos.trackingSdk.repositories.paging.PagingParamConfig

class TripPagingDataSource(
    private val tripDao: TripDao,
    private var payload: Int
) : PagingSource<Int, TripModel>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, com.kerberos.trackingSdk.models.TripModel> {
        return try {
            // Delegate loading to the DAO's PagingSource and map entities to TripModel
            val source = tripDao.getTripsPaging()
            val result = source.load(params)

            when (result) {
                is LoadResult.Page -> {
                    val data = result.data.map { trip ->
                        com.kerberos.trackingSdk.models.TripModel(
                            id = trip.id,
                            tripDuration = trip.tripDuration,
                            startTime = trip.startTime,
                            totalDistance = trip.totalDistance,
                            endTime = trip.endTime,
                            isActive = trip.isActive
                        )
                    }
                    // Propagate prevKey / nextKey from the underlying result
                    LoadResult.Page(
                        data = data,
                        prevKey = result.prevKey,
                        nextKey = result.nextKey
                    )
                }
                is LoadResult.Error -> LoadResult.Error(result.throwable)
                is LoadResult.Invalid -> LoadResult.Invalid()
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TripModel> {
//        val pageNumber = params.key ?: PagingParamConfig.initialOffset
//        return try {
//            val tripEntities = tripDao.getTripsPaging()
//            val result = tripEntities.load(
//                LoadParams.Append(
//                    key = pageNumber,
//                    loadSize = params.loadSize,
//                    placeholdersEnabled = false
//                )
//            )
//            val data = if (result is LoadResult.Page) {
//                result.data.map { trip ->
//                    TripModel(
//                        id = trip.id,
//                        tripDuration = trip.tripDuration,
//                        startTime = trip.startTime,
//                        totalDistance = trip.totalDistance,
//                        endTime = trip.endTime,
//                        isActive = trip.isActive
//                    )
//                }
//            } else {
//                emptyList()
//            }
//
//            val nextPagePayload = params.key
////            nextPagePayload?.inc()
////            result.load(params)
//
//            LoadResult.Page(
//                data = data,
//                prevKey = pageNumber,
//                nextKey = if (data.isEmpty()) null else nextPagePayload,
//                //nextKey = pageNumber + (params.loadSize / PagingParamConfig.pageSize)
//
//            )
//
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }

    override fun getRefreshKey(state: PagingState<Int, com.kerberos.trackingSdk.models.TripModel>): Int? {
        // Standard anchor-based refresh key calculation
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        } ?: PagingParamConfig.initialOffset
    }

    override val keyReuseSupported: Boolean
        get() = true

//    override fun getRefreshKey(state: PagingState<Int, TripModel>): Int {
////        payload.set(PagingParamConfig.initialOffset)
//        payload = PagingParamConfig.initialOffset
//        return PagingParamConfig.initialOffset
//    }

//    override fun getRefreshKey(state: PagingState<Int, TripModel>): Int? {
//        // Try to find the page key of the closest page to anchorPosition, from
//        // either the prevKey or the nextKey, but you need to handle nullability
//        // here:
//        //  * prevKey == null -> anchorPage is the first page.
//        //  * nextKey == null -> anchorPage is the last page.
//        //  * both prevKey and nextKey null -> anchorPage is the only page.
//        return state.anchorPosition?.let { anchorPosition ->
//            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//        }
//    }

}