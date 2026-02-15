// Copyright (c) 2025 - 2026 Dietmar Planitzer <https://www.linkedin.com/in/dplanitzer/>
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
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

    // Sends a GraphQL query to the GraphQL server and returns the result of that query. An
    // exception is thrown if a network or other kind of error is encountered.
    suspend fun query(address: BusinessAddress, category: String, sortBy: String, offset: Int, limit: Int): QueryResult {

        Log.d(
            TAG,
            "Query(\"$address\", cat: \"$category\", sort: \"$sortBy\", offset: $offset, limit: $limit)"
        )

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

        val r = client.query(
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

        if (r.exception != null) {
            throw r.exception!!
        }

        val businesses = r.data?.search?.business?.map { Business(it!!) }
        val totalCount = r.data?.search?.total ?: 0

        return QueryResult(totalCount, businesses ?: listOf())
    }

    companion object {
        private const val TAG = "BusinessRepository"
    }
}