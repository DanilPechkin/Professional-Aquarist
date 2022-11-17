package com.danilp.professionalaquarist.android.screens.aquarium.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.screens.AquariumTopBarWithSearch
import com.danilp.professionalaquarist.android.screens.GridItem
import com.danilp.professionalaquarist.android.screens.destinations.AquariumEditDestination
import com.danilp.professionalaquarist.android.screens.destinations.MainAquariumScreenDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun AquariumList(
    navigator: DestinationsNavigator,
    viewModel: AquariumListViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)
    var isSearchFieldVisible by remember { mutableStateOf(false) }
    val searchFieldFocusRequester = remember { FocusRequester() }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberLazyGridState()

    LaunchedEffect(key1 = true) {
        viewModel.onEvent(AquariumListEvent.Refresh)
    }

    Scaffold(
        topBar = {
            AquariumTopBarWithSearch(
                stringResource(R.string.aquariums_top_bar_title),
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    viewModel.onEvent(AquariumListEvent.OnSearchQueryChange(it))
                },
                isSearchFieldVisible = isSearchFieldVisible,
                switchSearchFieldVisibility = { isSearchFieldVisible = !isSearchFieldVisible },
                hideSearchField = { isSearchFieldVisible = false },
                searchFieldFocusRequester = searchFieldFocusRequester,
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigator.navigate(
                        AquariumEditDestination(-1L)
                    )
                },
                icon = { Icon(Icons.Rounded.Add, stringResource(R.string.add_aquarium_fab)) },
                text = { Text(stringResource(R.string.add_aquarium_fab)) },
                expanded = !scrollState.isScrollInProgress
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 8.dp)
        ) {

            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.onEvent(AquariumListEvent.Refresh) }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    state = scrollState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.aquariums,
                        key = { it.id!! }
                    ) { aquarium ->
                        GridItem(
                            label = aquarium.name,
                            imageUrl = aquarium.imageUrl,
                            message = aquarium.minTemperature.toString() + " " +
                                    aquarium.maxTemperature.toString(),
                            modifier = Modifier
                                .animateItemPlacement()
                                .clickable {
                                    viewModel.onEvent(
                                        AquariumListEvent.OnAquariumClicked(aquarium.id!!)
                                    )
                                    navigator.navigate(
                                        MainAquariumScreenDestination()
                                    )
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
