//
// Copyright 2023, Dietmar Planitzer
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