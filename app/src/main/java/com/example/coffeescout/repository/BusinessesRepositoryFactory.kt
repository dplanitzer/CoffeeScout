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
                },
                {
                  "day": 5,
                  "end": "1700",
                  "start": "1000"
                },
                {
                  "day": 6,
                  "end": "1600",
                  "start": "1000"
                }
              ]
            }
          ],
          "location": {
            "address1": "236 Pike St",
            "city": "Seattle"
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
          "distance": 100,
          "hours": [
            {
              "is_open_now": true,
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
                },
                {
                  "day": 5,
                  "end": "1700",
                  "start": "1000"
                },
                {
                  "day": 6,
                  "end": "1600",
                  "start": "1000"
                }
              ]
            }
          ],
          "location": {
            "address1": "236 Pine St",
            "city": "Seattle"
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
          "distance": 210,
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
                },
                {
                  "day": 5,
                  "end": "1700",
                  "start": "1000"
                },
                {
                  "day": 6,
                  "end": "1600",
                  "start": "1000"
                }
              ]
            }
          ],
          "location": {
            "address1": "6 Pike St",
            "city": "Seattle"
          },
          "name": "Cafe Sima",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/AHdlJUFm-CWCAdB1SYOVfg/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 5,
          "review_count": 510,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        },
        {
          "distance": 10,
          "hours": [
            {
              "is_open_now": true,
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
                },
                {
                  "day": 5,
                  "end": "1700",
                  "start": "1000"
                },
                {
                  "day": 6,
                  "end": "1600",
                  "start": "1000"
                }
              ]
            }
          ],
          "location": {
            "address1": "16 Vine St",
            "city": "Seattle"
          },
          "name": "Cafe Hari",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/0-P7JPBXX-rWiAzS9pPv3Q/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 5,
          "review_count": 310,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        },
        {
          "distance": 810,
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
                },
                {
                  "day": 5,
                  "end": "1700",
                  "start": "1000"
                },
                {
                  "day": 6,
                  "end": "1600",
                  "start": "1000"
                }
              ]
            }
          ],
          "location": {
            "address1": "18 Vine St",
            "city": "Seattle"
          },
          "name": "Cafe Mali",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/bEOl2qKICwl9-mJ8wmzLIg/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 3.5,
          "review_count": 120,
          "url": "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
          "id": "Jpxv-URBpIvcu0mE5B6S3g",
          "coordinates": {
            "latitude": 37.77826,
            "longitude": -122.39388
          }
        },
        {
          "distance": 312,
          "hours": [
            {
              "is_open_now": true,
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
                },
                {
                  "day": 5,
                  "end": "1700",
                  "start": "1000"
                },
                {
                  "day": 6,
                  "end": "1600",
                  "start": "1000"
                }
              ]
            }
          ],
          "location": {
            "address1": "16 Pine St",
            "city": "Seattle"
          },
          "name": "Cafe Hermagor",
          "photos": [
            "https://s3-media0.fl.yelpcdn.com/bphoto/Z4r50030WAO5JWehJlTJ4w/o.jpg"
          ],
          "price": "${'$'}${'$'}",
          "rating": 3.5,
          "review_count": 50,
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