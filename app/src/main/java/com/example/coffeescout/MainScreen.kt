package com.example.coffeescout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coffeescout.repository.Business

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(),
               initialStreetAddress: String,
               businessFormatter: BusinessFormatter,
               onAction: BusinessCardActionCallback = { _, _ -> },
               onAddressChange: (String, LazyPagingItems<Business>) -> Unit = { _, _ -> }
) {
    val lazyPagingItems = viewModel.businessFlow.collectAsLazyPagingItems()

    Column {
        AddressInputBar(
            initialStreetAddress = initialStreetAddress,
            onAddressChange = { newAddress ->
                onAddressChange(newAddress, lazyPagingItems)
            }
        )

        LazyBusinessColumn(
            lazyPagingItems,
            modifier = Modifier.padding(top = 16.dp),
            businessFormatter,
            onAction
        )
    }
}

@Composable
fun AddressInputBar(
    initialStreetAddress: String,
    onAddressChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf(initialStreetAddress) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(colorResource(R.color.purple_500))
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            placeholder = { Text(stringResource(R.string.address_input_hint)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = colorResource(R.color.purple_200),
                disabledContainerColor = Color.Gray
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(
                onGo = {
                    focusManager.clearFocus()
                    keyboardController?.hide()

                    onAddressChange(text)
                }
            )
        )
    }
}

@Composable
fun LazyBusinessColumn(
    lazyPagingItems: LazyPagingItems<Business>,
    modifier: Modifier = Modifier,
    businessFormatter: BusinessFormatter,
    onAction: BusinessCardActionCallback
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(lazyPagingItems.itemCount) { index ->
            val business = lazyPagingItems[index]

            if (business != null) {
                BusinessCard(business, businessFormatter, onAction)
            }
        }

        // Handle loading states (optional, but recommended for good UX)
        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                    item {
                        LoadingIndicator(
                            Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight()
                        )
                    }
                }
                /* loadState.append is LoadState.Loading -> {
                item {
                    LoadingIndicator(
                        Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight()
                ) }
            }*/
                loadState.refresh is LoadState.Error -> {
                    //val e = loadState.refresh as LoadState.Error
                    item {
                        ErrorView(
                            onRetry = { lazyPagingItems.retry() },
                            modifier = Modifier
                                        .fillMaxWidth()
                                        .fillParentMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator()
    }
}