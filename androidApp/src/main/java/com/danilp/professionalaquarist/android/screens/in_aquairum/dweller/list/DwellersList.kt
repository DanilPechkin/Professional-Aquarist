package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.professionalaquarist.android.screens.AquariumTopBarWithSearch
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun DwellersList(
    navigator: DestinationsNavigator,
    viewModel: DwellersListViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = state.isRefreshing)
    var isSearchFieldVisible by remember { mutableStateOf(false) }
    val searchFieldFocusRequester = remember { FocusRequester() }
    var isTopMenuExpanded by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            AquariumTopBarWithSearch(
                stringResource(R.string.dwellers_title),
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    viewModel.onEvent(DwellersListEvent.OnSearchQueryChange(it))
                },
                isSearchFieldVisible = isSearchFieldVisible,
                switchSearchFieldVisibility = { isSearchFieldVisible = !isSearchFieldVisible },
                hideSearchField = { isSearchFieldVisible = false },
                searchFieldFocusRequester = searchFieldFocusRequester,
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigate(AquariumListDestination()) },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = {  }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    navigator.navigate(DwellerEditDestination(0))
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.add_dweller_button)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = { viewModel.onEvent(DwellersListEvent.Refresh) }
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.dwellers) { dweller ->
                        DwellersListItem(
                            dweller = dweller,
                            modifier = Modifier.clickable {
                                navigator.navigate(DwellerEditDestination(dweller.id))
                            }
                        )
                    }
                }
            }
        }
    }
}