package com.example.coffeescout.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coffeescout.LocationPermissionDenied
import com.example.coffeescout.R
import java.io.IOException

@Composable
fun ErrorView(
    exception: Throwable,
    onRetry: () -> Unit = {},
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(messageId(exception)),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 32.dp)
        )

        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry_title))
        }
    }
}

private fun messageId(ex: Throwable): Int {
    return when(ex) {
        is LocationPermissionDenied -> R.string.loc_permission_error_msg
        else -> R.string.network_error_msg
    }
}


@Preview()
@Composable
private fun ErrorViewPreview() {
    MaterialTheme {
        ErrorView(
            exception = IOException("Some network error"),
            modifier = Modifier
        )
    }
}