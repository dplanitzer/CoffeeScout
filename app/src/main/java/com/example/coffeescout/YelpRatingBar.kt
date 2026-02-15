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
package com.example.coffeescout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.max
import kotlin.math.min

// A Yelp style rating bar. See the display requirements here:
// https://www.yelp.com/developers/display_requirements
@Composable
fun YelpRatingBar(rating: Double) {

    Image(
        painter = painterResource(id = drawableIdForRating(rating)),
        contentDescription = stringResource(id = R.string.rating_content_desc, rating),
        modifier = Modifier.wrapContentSize()
    )
}

// Returns the stars drawable corresponding to the current rating value
private fun drawableIdForRating(rating: Double): Int  {
    val r = max(0.0, min(5.0, rating))
    val ir = r.toInt()
    val isHalf = (r - ir.toDouble()) >= 0.5

    return if (!isHalf) {
        stars[ir]
    } else {
        starsHalf[max(1, min(4, ir)) - 1]
    }
}

private val stars = listOf(
    R.drawable.stars_small_0,
    R.drawable.stars_small_1,
    R.drawable.stars_small_2,
    R.drawable.stars_small_3,
    R.drawable.stars_small_4,
    R.drawable.stars_small_5
)

private val starsHalf = listOf(
    R.drawable.stars_small_1_half,
    R.drawable.stars_small_2_half,
    R.drawable.stars_small_3_half,
    R.drawable.stars_small_4_half
)


@Preview()
@Composable
private fun YelpRatingBarPreview() {
    MaterialTheme {
        YelpRatingBar(0.5)
    }
}