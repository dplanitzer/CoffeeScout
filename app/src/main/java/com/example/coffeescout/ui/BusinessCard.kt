package com.example.coffeescout.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.coffeescout.BusinessFormatter
import com.example.coffeescout.R
import com.example.coffeescout.ui.YelpRatingBar
import com.example.coffeescout.repository.Business
import java.time.DayOfWeek

// The actions that a user might trigger by tapping on the various buttons in a card
enum class BusinessCardAction {
    GoToReviews,        // go to the business reviews page
    GoToDetails,        // go to the business details page
    GoToNavigation,     // go to the navigation app
    GoToYelp,           // go to the Yelp website
}


// The business card action handler. Receives the business card action and the business object on
// which the callback should act.
typealias BusinessCardActionCallback = (BusinessCardAction, Business) -> Unit


@Composable
fun BusinessCard(b: Business,
                 businessFormatter: BusinessFormatter,
                 onAction: BusinessCardActionCallback
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LandscapeCard(
                b,
                businessFormatter,
                onAction
            )
        } else {
            PortraitCard(
                b,
                businessFormatter,
                onAction
            )
        }
    }
}

@Composable
private fun PortraitCard(b: Business,
                         businessFormatter: BusinessFormatter,
                         onAction: BusinessCardActionCallback
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = b.heroImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clickable {
                    onAction(BusinessCardAction.GoToDetails, b)
                },
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.hero_content_desc)
        )

        InfoBlock(
            b,
            businessFormatter,
            onAction
        )
    }
}

@Composable
private fun LandscapeCard(b: Business,
                          businessFormatter: BusinessFormatter,
                          onAction: BusinessCardActionCallback
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = b.heroImageUrl,
            modifier = Modifier
                .width(400.dp)
                .height(260.dp)
                .clickable {
                    onAction(BusinessCardAction.GoToDetails, b)
                },
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.hero_content_desc)
        )

        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
        ) {
            InfoBlock(
                b,
                businessFormatter,
                onAction
            )
        }
    }
}

@Composable
private fun InfoBlock(b: Business,
                      businessFormatter: BusinessFormatter,
                      onAction: BusinessCardActionCallback
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = b.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 12.dp)
        )

        if (b.displayAddress.isNotEmpty()) {
            Row(
                Modifier.fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Place,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp).padding(end = 6.dp)
                )

                Text(
                    text = b.displayAddress,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.68f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp)
        ) {
            YelpRatingBar(b.rating)

            Text(
                text = businessFormatter.formatInfo(b, stringResource(R.string.bar_sep)),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.68f),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 8.dp)
            )
        }

        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = colorResource(
                            if (b.hours.isOpenNow) R.color.business_open else R.color.business_closed
                        )
                    )
                ) {
                    append(stringResource(if (b.hours.isOpenNow) R.string.business_open else R.string.business_closed))
                }

                val openingTimesString = businessFormatter.formatOpeningTimes(
                    b.hours.openingTimes,
                    stringResource(R.string.dash_sep)
                )
                if (openingTimesString.isNotEmpty()) {
                    append(stringResource(R.string.bullet_sep))
                    append(openingTimesString)
                }
            },
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.68f),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(vertical = 16.dp)
        ) {
            OutlinedButton(onClick = {
                onAction(BusinessCardAction.GoToReviews, b)
            }) {
                Text(stringResource(R.string.read_reviews_title))
            }

            Spacer(
                modifier = Modifier.weight(0.5f)
            )

            Image(
                painter = painterResource(R.drawable.yelp_logo),
                contentDescription = "",
                modifier = Modifier
                    .width(72.dp)
                    .padding(horizontal = 10.dp)
                    .clickable {
                        onAction(BusinessCardAction.GoToYelp, b)
                    }
            )

            Spacer(
                modifier = Modifier.weight(0.5f)
            )

            OutlinedButton(onClick = {
                onAction(BusinessCardAction.GoToNavigation, b)
            }) {
                Text(stringResource(R.string.navigation_title))
            }
        }
    }
}


@Preview(name = "Portrait", widthDp = 360, heightDp = 600)
@Preview(name = "Landscape", widthDp = 600, heightDp = 360)
@Composable
private fun BusinessCardPreview() {
    val businessFormatter = BusinessFormatter(LocalResources.current)

    MaterialTheme {
        BusinessCard(
            Business(
                id = "Jpxv-URBpIvcu0mE5B6S3g",
                name = "Cafe Sima",
                displayAddress = "236 Pine St",
                heroImageUrl = "https://s3-media1.ak.yelpcdn.com/bphoto/ehZk1zXTE5xof4d2fcGLeQ/o.jpg",
                detailsPageUrl = "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
                displayPricingLevel = "$$",
                rating = 1.5,
                reviewCount = 268,
                distanceInMeters = 491.0,
                hours = Business.Hours(
                    isOpenNow = false,
                    openingTimes = listOf(
                        Business.OpeningTime(
                            DayOfWeek.MONDAY,
                            Business.HoursMinutes(10, 30),
                            Business.HoursMinutes(19, 30)
                        )
                    )
                ),
                geoLocation = null,
                reviewsUrl = ""
            ),
            businessFormatter, { _, _ -> }
        )
    }
}