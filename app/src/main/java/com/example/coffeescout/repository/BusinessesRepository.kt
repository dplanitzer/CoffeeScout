//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.ApolloRequest
import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Optional
import com.apollographql.apollo.interceptor.ApolloInterceptor
import com.apollographql.apollo.interceptor.ApolloInterceptorChain
import com.apollographql.apollo.network.okHttpClient
import com.example.coffeescout.BuildConfig
import com.example.coffeescout.GetBusinessesQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient

// A repository that offers API to query for businesses close to a user defined address.
class BusinessesRepository(private val graphQlUrl: String, interceptor: Interceptor? = null) {

    // The result of a queryBusinessesSync() call
    data class QueryResult(val totalCount: Int, val businesses: List<Business>)


    // OkHttp interceptor to provide the Yelp API key in the "Authorization" header
    private class AuthInterceptor : ApolloInterceptor {

        override fun <D : Operation.Data> intercept(request: ApolloRequest<D>, chain: ApolloInterceptorChain): Flow<ApolloResponse<D>> {
            val authenticatedRequest = request.newBuilder()
                .addHttpHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
                .build()

            return chain.proceed(authenticatedRequest)
        }
    }

    private fun buildOkHttpClient(interceptor: Interceptor?): OkHttpClient {
        return if (interceptor != null) {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        } else {
            OkHttpClient.Builder()
                .build()
        }
    }

    private val client: ApolloClient = ApolloClient.Builder()
        .serverUrl(graphQlUrl)
        .addInterceptor(AuthInterceptor())
        .okHttpClient(buildOkHttpClient(interceptor))
        .build()

    private val lock = Any()

    // Returns the most recently encountered error. Null is returned if the most recent query has
    // worked without any error.
    // Thread safe
    var mostRecentError: Exception?
        get() = synchronized(lock) { return _mostRecentError }
        private set(value) = synchronized(lock) { _mostRecentError = value }
    private var _mostRecentError: Exception? = null

    // Sends a GraphQL query to the GraphQL server and returns the result of that query. An
    // exception is thrown if a network or other kind of error is encountered.
    fun queryBusinessesSync(address: BusinessAddress, category: String, sortBy: String, offset: Int, limit: Int): QueryResult {

        try {
            val r = runBlocking {
                Log.d(TAG, "Query(\"$address\", cat: \"$category\", sort: \"$sortBy\", offset: $offset, limit: $limit)")

                val location: Optional<String>
                val latitude: Optional<Double>
                val longitude: Optional<Double>

                when (address) {
                    is BusinessAddress.Address -> {
                        location = Optional.present(address.data)
                        latitude = Optional.absent()
                        longitude = Optional.absent()
                    }
                    is BusinessAddress.Location -> {
                        location = Optional.absent()
                        latitude = Optional.present(address.latitude)
                        longitude = Optional.present(address.longitude)
                    }
                }

                client.query(
                    GetBusinessesQuery(
                        location,
                        longitude,
                        latitude,
                        Optional.present(category),
                        Optional.present(offset),
                        Optional.present(limit),
                        Optional.present(sortBy)
                    )
                ).execute()
            }

            if (r.exception != null) {
                throw r.exception!!
            }

            val businesses = r.data?.search?.business?.map { Business(it!!) }
            val totalCount = r.data?.search?.total ?: 0
            mostRecentError = null

            return QueryResult(totalCount, businesses ?: listOf())
        } catch(ex: Exception) {
            // Record the exception and rethrow it
            mostRecentError = ex
            throw ex
        }
    }

    companion object {
        private const val TAG = "BusinessRepository"
    }
}