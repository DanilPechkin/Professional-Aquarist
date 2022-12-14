package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
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
import com.danilp.professionalaquarist.android.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.professionalaquarist.android.screens.destinations.AquariumListDestination
import com.danilp.professionalaquarist.android.screens.destinations.DwellerEditDestination
import com.danilp.professionalaquarist.android.screens.destinations.DwellerInfoDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBarWithSearch
import com.danilp.professionalaquarist.android.ui.GridItem
import com.danilp.professionalaquarist.android.ui.GridTitle
import com.danilp.professionalaquarist.android.ui.isScrollingUp
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerTags
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

    var isSearchFieldVisible by remember { mutableStateOf(false) }
    val searchFieldFocusRequester = remember { FocusRequester() }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberLazyGridState()

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
                navigateToAccount = { }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navigator.navigate(
                        DwellerEditDestination(-1)
                    )
                },
                icon = { Icon(Icons.Rounded.Add, stringResource(R.string.add_dweller_button)) },
                text = { Text(stringResource(R.string.add_dweller_button)) },
                expanded = scrollState.isScrollingUp()
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(top = 8.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp, start = 16.dp, end = 16.dp),
                state = scrollState,
                modifier = Modifier.fillMaxSize()
            ) {
                if (state.dwellers.any { it.tags?.contains(DwellerTags.FISH.code) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.fish_title)
                        )
                    }
                    items(
                        state.dwellers.filter {
                            it.tags?.contains(DwellerTags.FISH.code) ?: false
                        }
                    ) { dweller ->
                        GridItem(
                            label = dweller.name,
                            message = dweller.status ?: "",
                            imageUrl = dweller.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(DwellerInfoDestination(dweller.id!!))
                                }
                        )
                    }
                }
                if (state.dwellers.any { it.tags?.contains(DwellerTags.SHRIMP.code) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.shrimp_title),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(
                        state.dwellers.filter {
                            it.tags?.contains(DwellerTags.SHRIMP.code) ?: false
                        }
                    ) { dweller ->
                        GridItem(
                            label = dweller.name,
                            message = dweller.status ?: "",
                            imageUrl = dweller.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(DwellerInfoDestination(dweller.id!!))
                                }
                        )
                    }
                }
                if (state.dwellers.any { it.tags?.contains(DwellerTags.CRAB.code) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.crab_title),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(
                        state.dwellers.filter {
                            it.tags?.contains(DwellerTags.CRAB.code) ?: false
                        }
                    ) { dweller ->
                        GridItem(
                            label = dweller.name,
                            message = dweller.status ?: "",
                            imageUrl = dweller.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(DwellerInfoDestination(dweller.id!!))
                                }
                        )
                    }
                }
                if (state.dwellers.any { it.tags?.contains(DwellerTags.CRAYFISH.code) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.crayfish_title),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(
                        state.dwellers.filter {
                            it.tags?.contains(DwellerTags.CRAYFISH.code) ?: false
                        }
                    ) { dweller ->
                        GridItem(
                            label = dweller.name,
                            message = dweller.status ?: "",
                            imageUrl = dweller.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(DwellerInfoDestination(dweller.id!!))
                                }
                        )
                    }
                }
                if (state.dwellers.any { it.tags?.contains(DwellerTags.SNAIL.code) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.snail_title),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(
                        state.dwellers.filter {
                            it.tags?.contains(DwellerTags.SNAIL.code) ?: false
                        }
                    ) { dweller ->
                        GridItem(
                            label = dweller.name,
                            message = dweller.status ?: "",
                            imageUrl = dweller.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(DwellerInfoDestination(dweller.id!!))
                                }
                        )
                    }
                }
                if (state.dwellers.any { it.tags?.contains(DwellerTags.BIVALVE.code) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.bivalve_title),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    items(
                        state.dwellers.filter {
                            it.tags?.contains(DwellerTags.BIVALVE.code) ?: false
                        }
                    ) { dweller ->
                        GridItem(
                            label = dweller.name,
                            message = dweller.status ?: "",
                            imageUrl = dweller.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(DwellerInfoDestination(dweller.id!!))
                                }
                        )
                    }
                }
            }
        }
    }
}
