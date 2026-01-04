//
// Copyright 2025, Dietmar Planitzer
//
package com.example.coffeescout.repository

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

fun createBusinessRepository(): BusinessesRepository {
    return if (false) {
        BusinessesRepository(YELP_GRAPHQL_URL)
    } else {
        createMockedBusinessRepository()
    }
}

private fun createMockedBusinessRepository(): BusinessesRepository {
    class MockInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            return Response.Builder()
                .header("Content-Type", "application/json")
                .body("""{
  "data": {
    "search": {
      "total": 6,
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
        },
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
            "address1": "236 Pine St",
            "city": "San Francisco"
          },
          "name": "Cafe Sima",
          "photos": [
            "https://s3-media1.ak.yelpcdn.com/bphoto/ehZk1zXTE5xof4d2fcGLeQ/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 1.5,
          "review_count": 268,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        },
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
            "address1": "6 Urban St",
            "city": "San Francisco"
          },
          "name": "Cafe Sima",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/AHdlJUFm-CWCAdB1SYOVfg/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 5,
          "review_count": 268,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        },
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
            "address1": "16 Vine St",
            "city": "San Francisco"
          },
          "name": "Cafe Hari",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/0-P7JPBXX-rWiAzS9pPv3Q/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 5,
          "review_count": 268,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        },
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
            "address1": "18 Vine St",
            "city": "San Francisco"
          },
          "name": "Cafe Mali",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/bEOl2qKICwl9-mJ8wmzLIg/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 3.5,
          "review_count": 268,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        },
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
            "address1": "16 X St",
            "city": "San Francisco"
          },
          "name": "Cafe Hermagor",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/Z4r50030WAO5JWehJlTJ4w/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 3.5,
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

    return BusinessesRepository("http://127.0.0.1/graphql", MockInterceptor())
}

private const val YELP_GRAPHQL_URL = "https://api.yelp.com/v3/graphql"