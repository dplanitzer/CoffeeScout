//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.BusinessesRepository

class BusinessesDataSource(
    private val repository: BusinessesRepository,
    private val userAddress: String,
    private val category: String,
    private val sortBy: String
    ) : PagingSource<Int, Business>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Business> {

        return try {
            val offset = params.key ?: 0
            val qr = repository.queryBusinessesSync(userAddress, category, sortBy, offset, params.loadSize)
            val prevKey = if (offset > 0) offset - params.loadSize else null
            val nextKey = if (qr.totalCount >= params.loadSize) offset + params.loadSize else null

            LoadResult.Page(
                data = qr.businesses,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (ex: Exception) {
            Log.e(TAG, "load: ${ex.message}")
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Business>): Int? {
        return state.anchorPosition
    }


    companion object {
        private const val TAG = "BusinessDataSource"
    }
}
