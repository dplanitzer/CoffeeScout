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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.coffeescout.repository.Business

@Composable
fun MainScreen(initialStreetAddress: String,
               businessFormatter: BusinessFormatter,
               modifier: Modifier = Modifier,
               viewModel: MainViewModel = viewModel(),
               onAction: BusinessCardActionCallback = { _, _ -> },
               onAddressChange: (String, LazyPagingItems<Business>) -> Unit = { _, _ -> }
) {
    val lazyPagingItems = viewModel.businessFlow.collectAsLazyPagingItems()

    Box(modifier = modifier) {
        LazyBusinessColumn(
            lazyPagingItems,
            businessFormatter,
            onAction
        )

        AddressInputBar(
            initialStreetAddress = initialStreetAddress,
            onAddressChange = { newAddress ->
                onAddressChange(newAddress, lazyPagingItems)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressInputBar(
    initialStreetAddress: String,
    onAddressChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var query by rememberSaveable { mutableStateOf(initialStreetAddress) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .blur(
                radius = 32.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
            )
            .padding(bottom = 10.dp)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {
                        focusManager.clearFocus()
                        keyboardController?.hide()

                        onAddressChange(query)
                    },
                    expanded = false,
                    onExpandedChange = { },
                    placeholder = { Text(stringResource(R.string.address_input_hint)) },
                    leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {},
                )
            },
            expanded = false,
            onExpandedChange = { },
            content = {},
            windowInsets = WindowInsets(top = 0.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp)
        )
    }
}

@Composable
fun LazyBusinessColumn(
    lazyPagingItems: LazyPagingItems<Business>,
    businessFormatter: BusinessFormatter,
    onAction: BusinessCardActionCallback,
    modifier: Modifier = Modifier
    ) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 60.dp + 16.dp, bottom = 16.dp, start = 12.dp, end = 12.dp)
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
                    val e = loadState.refresh as LoadState.Error
                    item {
                        ErrorView(
                            exception = e.error,
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