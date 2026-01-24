package com.example.coffeescout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel(),
               businessFormatter: BusinessFormatter,
               actionHandler: BusinessCardActionCallback
) {
    val lazyPagingItems = viewModel.businessFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(lazyPagingItems.itemCount) { index ->
            val business = lazyPagingItems[index]

            if (business != null) {
                BusinessCard(business, businessFormatter, actionHandler)
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
                        ) }
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
                    item { ErrorView { viewModel.invalidate() } }
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