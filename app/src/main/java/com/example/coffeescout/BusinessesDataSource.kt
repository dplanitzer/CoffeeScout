//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.util.Log
import androidx.paging.DataSource
import androidx.paging.PositionalDataSource
import com.example.coffeescout.repository.Business
import com.example.coffeescout.repository.BusinessesRepository

// Our positional data source implementation to make the paging 2 library happy. This implementation
// uses our BusinessesRepository to load the data from the network. Note that the loadInitial()
// and loadRange() methods are executed on a thread from the Android I/O thread pool.
class BusinessesDataSource(
    private val repository: BusinessesRepository,
    private val userAddress: String,
    private val category: String,
    private val sortBy: String
    ) : PositionalDataSource<Business>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Business>) {

        try {
            val qr = repository.queryBusinessesSync(userAddress, category, sortBy, params.requestedStartPosition, params.requestedLoadSize)

            callback.onResult(qr.businesses, params.requestedStartPosition, qr.totalCount)
        } catch (ex: Exception) {
            Log.e(TAG, "loadInitial: ${ex.message}")
            callback.onResult(listOf(), params.requestedStartPosition, 0)
        }
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Business>) {

        try {
            val qr = repository.queryBusinessesSync(userAddress, category, sortBy, params.startPosition, params.loadSize)

            callback.onResult(qr.businesses)
        } catch (ex: Exception) {
            Log.e(TAG, "loadRange: ${ex.message}")
            callback.onResult(listOf())
        }
    }

    companion object {
        const private val TAG = "BusinessDataSource"
    }
}


class BusinessesDataSourceFactory(
    private val repository: BusinessesRepository,
    private val userAddress: String,
    private val category: String,
    private val sortBy: String
    ) : DataSource.Factory<Int, Business>() {

    override fun create(): DataSource<Int, Business> {

        return BusinessesDataSource(repository, userAddress, category, sortBy)
    }
}
