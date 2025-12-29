package com.example.coffeescout

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.coffeescout.repository.BusinessesRepository
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class BusinessesRepositoryInstrumentedTest {

    // Expects: that queryBusinessSync() throws an exception if it receives HTTP status != 200
    @Test
    fun queryBusinessesSync_InternalServerError() {

        class MockInterceptor : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                return Response.Builder()
                    .header("Content-Type", "plain/text")
                    .body("Oops".toResponseBody("plain/text".toMediaType()))
                    .code(500)
                    .message("Internal Server Error")
                    .protocol(Protocol.HTTP_1_0)
                    .request(chain.request())
                    .build()
            }
        }

        val rep = BusinessesRepository("http://127.0.0.1/graphql", MockInterceptor())
        try {
            rep.queryBusinessesSync("here", "this", "distance", 0, 10)
            fail("Expected an exception")
        } catch (_: Exception) {

        }
    }

    // Expects: that queryBusinessSync() receives and successfully parses a valid GraphQL query response
    @Test
    fun queryBusinessesSync_Success() {

        class MockInterceptor : Interceptor {

            override fun intercept(chain: Interceptor.Chain): Response {
                return Response.Builder()
                    .header("Content-Type", "application/json")
                    .body("""{
  "data": {
    "search": {
      "total": 1,
      "business": [
        {
          "distance": 491.02400910214897,
          "hours": [
            {
              "is_open_now": false,
              "open": [
                {
                  "day": 0,
                  "end": "1930",
                  "start": "1030"
                },
                {
                  "day": 1,
                  "end": "1930",
                  "start": "1030"
                },
                {
                  "day": 2,
                  "end": "1930",
                  "start": "1030"
                },
                {
                  "day": 3,
                  "end": "1930",
                  "start": "1030"
                },
                {
                  "day": 4,
                  "end": "1930",
                  "start": "1030"
                }
              ]
            }
          ],
          "location": {
            "address1": "236 Townsend St",
            "city": "San Francisco"
          },
          "name": "Cafe Okawari",
          "photos": [
            "https://s3-media4.fl.yelpcdn.com/bphoto/KLUzH1p9y8sFRrYeSB_LWg/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 4.5,
          "review_count": 268,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        }
      ]
    }
  }
}""".toResponseBody("application/json".toMediaType()))
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_0)
                    .request(chain.request())
                    .build()
            }
        }

        val rep = BusinessesRepository("http://127.0.0.1/graphql", MockInterceptor())
        try {
            val r = rep.queryBusinessesSync("here", "this", "distance", 0, 10)
            assertEquals(1, r.totalCount)
            assertEquals(1, r.businesses.size)

            val b = r.businesses.first()
            assertEquals("Jpxv-URBpIvcu0mE5B6S3g", b.id)
            assertEquals("Cafe Okawari", b.name)
        } catch (ex: Exception) {
            fail(ex.message)
        }
    }
}