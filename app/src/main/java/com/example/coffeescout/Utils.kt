//
// Copyright 2023, Dietmar Planitzer
//
package com.example.coffeescout

import android.app.Activity
import android.content.Intent
import android.icu.text.MeasureFormat
import android.icu.util.Measure
import android.icu.util.MeasureUnit
import android.net.Uri


// Invoke the web browser with the given URL.
fun Activity.openUrl(url: String): Boolean {

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    return if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        true
    } else {
        false
    }
}

fun Activity.openTurnByTurnNavigationTo(loc: GeoLocation): Boolean {

    val uri = Uri.parse("google.navigation:q=${loc.latitude},${loc.longitude}")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")

    return if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
        true
    } else {
        false
    }
}


// Formats the given metric distance in terms of the given unit
fun MeasureFormat.formatDistance(d: Double, unit: MeasureUnit): String {

    return formatMeasures(Measure(d, unit))
}
