package com.example.coffeescout

import android.content.res.Configuration
import android.icu.text.MeasureFormat
import android.icu.text.NumberFormat
import android.icu.util.MeasureUnit
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import coil.compose.AsyncImage
import com.example.coffeescout.repository.Business
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale


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
fun BusinessCardHolder(business: Business,
                       timeFormatter: DateTimeFormatter,
                       metersFormatter: MeasureFormat,
                       kilometersFormatter: MeasureFormat,
                       actionHandler: BusinessCardActionCallback
) {

    BusinessCard(business, timeFormatter, metersFormatter, kilometersFormatter, actionHandler)
}

@Composable
fun BusinessCard(b: Business,
                 timeFormatter: DateTimeFormatter,
                 metersFormatter: MeasureFormat,
                 kilometersFormatter: MeasureFormat,
                 actionHandler: BusinessCardActionCallback
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        LandscapeCard(
            b,
            timeFormatter,
            metersFormatter,
            kilometersFormatter,
            actionHandler
        )
    } else {
        PortraitCard(
            b,
            timeFormatter,
            metersFormatter,
            kilometersFormatter,
            actionHandler
        )
    }
}

@Composable
private fun PortraitCard(b: Business,
                         timeFormatter: DateTimeFormatter,
                         metersFormatter: MeasureFormat,
                         kilometersFormatter: MeasureFormat,
                         actionHandler: BusinessCardActionCallback
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        AsyncImage(
            model = b.heroImageUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clickable {
                    actionHandler(BusinessCardAction.GoToDetails, b)
                },
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.hero_content_desc)
        )

        InfoBlock(
            b,
            timeFormatter,
            metersFormatter,
            kilometersFormatter,
            actionHandler
        )
    }
}

@Composable
private fun LandscapeCard(b: Business,
                          timeFormatter: DateTimeFormatter,
                          metersFormatter: MeasureFormat,
                          kilometersFormatter: MeasureFormat,
                          actionHandler: BusinessCardActionCallback
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp)
    ) {
        AsyncImage(
            model = b.heroImageUrl,
            modifier = Modifier
                .width(400.dp)
                .height(230.dp)
                .padding(horizontal = 16.dp)
                .clickable {
                    actionHandler(BusinessCardAction.GoToDetails, b)
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
                timeFormatter,
                metersFormatter,
                kilometersFormatter,
                actionHandler
            )
        }
    }
}

@Composable
private fun InfoBlock(b: Business,
                      timeFormatter: DateTimeFormatter,
                      metersFormatter: MeasureFormat,
                      kilometersFormatter: MeasureFormat,
                      actionHandler: BusinessCardActionCallback
) {
    Text(
        text = b.name,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 8.dp)
    )

    Text(
        text = b.displayAddress,
        color = Color.DarkGray,
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
            .padding(top = 8.dp)
    ) {
        YelpRatingBar(b.rating)

        Text(
            text = formatBusinessInfo(b, metersFormatter, kilometersFormatter),
            color = Color.DarkGray,
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

            val openingTimesString = formatOpeningTimes(b.hours.openingTimes, timeFormatter)
            if (openingTimesString.isNotEmpty()) {
                append(stringResource(R.string.bullet_sep))
                append(openingTimesString)
            }
        },
        color = Color.DarkGray,
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
            actionHandler(BusinessCardAction.GoToReviews, b)
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
                    actionHandler(BusinessCardAction.GoToYelp, b)
                }
        )

        Spacer(
            modifier = Modifier.weight(0.5f)
        )

        OutlinedButton(onClick = {
            actionHandler(BusinessCardAction.GoToNavigation, b)
        }) {
            Text(stringResource(R.string.navigation_title))
        }
    }
}


// Returns a formatted string with the review count, pricing level and distance
@Composable
private fun formatBusinessInfo(b: Business, metersFormatter: MeasureFormat, kilometersFormatter: MeasureFormat): String {

    val buf = java.lang.StringBuilder()
    val barSep = stringResource(R.string.bar_sep)

    // (optional) review count
    if (b.reviewCount > 0) {
        buf.append(stringResource(R.string.reviews_count_format, b.reviewCount))
    }


    // (optional) pricing level
    if (b.displayPricingLevel.isNotEmpty()) {
        if (buf.isNotEmpty()) {
            buf.append(barSep)
        }
        buf.append(b.displayPricingLevel)
    }


    // (optional) distance
    if (b.distanceInMeters > 0) {
        if (buf.isNotEmpty()) {
            buf.append(barSep)
        }

        if (b.distanceInMeters >= 1000.0) {
            buf.append(kilometersFormatter.formatDistance(b.distanceInMeters / 1000.0, MeasureUnit.KILOMETER))
        } else {
            buf.append(metersFormatter.formatDistance(b.distanceInMeters, MeasureUnit.METER))
        }
    }

    return buf.toString()
}

// Returns a formatted string with the opening hours of the business. We only take the hours
// for today into account.
@Composable
private fun formatOpeningTimes(openingTimes: List<Business.OpeningTime>, timeFormatter: DateTimeFormatter): String {

    val todaysDayOfWeek = LocalDate.now().dayOfWeek
    val buf = java.lang.StringBuilder()

    for (ot in openingTimes) {
        if (ot.dayOfWeek == todaysDayOfWeek) {
            buf.append(formatHoursAndMinutes(ot.openingAt, timeFormatter))
            buf.append(stringResource(R.string.dash_sep))
            buf.append(formatHoursAndMinutes(ot.closingAt, timeFormatter))
            break
        }
    }

    return buf.toString()
}

// Returns a formatted string of the given hours and minutes. They are assumed to be in
// 24 hour format.
private fun formatHoursAndMinutes(t: Business.HoursMinutes, timeFormatter: DateTimeFormatter): String {

    return timeFormatter.format(LocalTime.of(t.hours, t.minutes))
}


@Preview(name = "Portrait", widthDp = 360, heightDp = 600)
@Preview(name = "Landscape", widthDp = 600, heightDp = 360)
@Composable
private fun BusinessCardPreview() {
    val curLocale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0] ?: Locale.getDefault()
    val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    val metersFormatter = MeasureFormat.getInstance(curLocale, MeasureFormat.FormatWidth.NARROW, NumberFormat.getIntegerInstance(curLocale))
    val kilometersNumberFormatter = NumberFormat.getNumberInstance(curLocale)
    kilometersNumberFormatter.minimumFractionDigits = 0
    kilometersNumberFormatter.maximumFractionDigits = 1
    val kilometersFormatter = MeasureFormat.getInstance(curLocale, MeasureFormat.FormatWidth.NARROW, kilometersNumberFormatter)

    MaterialTheme {
        BusinessCard(
            Business(
                id = "Jpxv-URBpIvcu0mE5B6S3g",
                name = "Cafe Sima",
                displayAddress = "236 Pine St, San Francisco",
                heroImageUrl = "https://s3-media1.ak.yelpcdn.com/bphoto/ehZk1zXTE5xof4d2fcGLeQ/o.jpg",
                detailsPageUrl = "https://www.yelp.com/biz/cafe-okawari-san-francisco?adjust_creative=GPmwJFx9er_qLz3BRWXviw&utm_campaign=yelp_api_v3&utm_medium=api_v3_graphql&utm_source=GPmwJFx9er_qLz3BRWXviw",
                displayPricingLevel = "$$",
                rating = 1.5,
                reviewCount = 268,
                distanceInMeters = 491.0,
                hours = Business.Hours(
                    isOpenNow = false,
                    openingTimes = listOf(Business.OpeningTime(DayOfWeek.MONDAY, Business.HoursMinutes(10, 30), Business.HoursMinutes(19, 30)))
                ),
                geoLocation = null,
                reviewsUrl = ""
            ),
            timeFormatter, metersFormatter, kilometersFormatter, { _, _ -> }
        )
    }
}