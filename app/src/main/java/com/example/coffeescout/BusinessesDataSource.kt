//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.BusinessAddress
import com.example.coffeescout.repository.BusinessesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max

class BusinessesDataSource(
    private val repository: BusinessesRepository,
    private val address: String,
    private val onResolveAddress: suspend (String) -> BusinessAddress,
    private val category: String,
    private val sortBy: String
    ) : PagingSource<Int, Business>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Business> {

        return try {
            val addr = withContext(Dispatchers.Main) {
                onResolveAddress(address)
            }

            val offset = params.key ?: 0
            val qr = repository.query(addr, category, sortBy, offset, params.loadSize)
            val prevKey = if (offset > 0) max(offset - params.loadSize, 0) else null
            val nextKey = if (offset + qr.businesses.size < qr.totalCount) offset + qr.businesses.size else null

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
