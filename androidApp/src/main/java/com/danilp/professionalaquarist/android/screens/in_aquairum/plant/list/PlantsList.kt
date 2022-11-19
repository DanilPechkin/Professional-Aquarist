package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.list

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
import com.danilp.professionalaquarist.android.screens.destinations.PlantEditDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBarWithSearch
import com.danilp.professionalaquarist.android.ui.GridItem
import com.danilp.professionalaquarist.android.ui.GridTitle
import com.danilp.professionalaquarist.android.ui.isScrollingUp
import com.danilp.professionalaquarist.domain.plant.tags.PlantTags
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun PlantsList(
    navigator: DestinationsNavigator,
    viewModel: PlantsListViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isSearchFieldVisible by remember { mutableStateOf(false) }
    val searchFieldFocusRequester = remember { FocusRequester() }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberLazyGridState()

    Scaffold(
        topBar = {
            AquariumTopBarWithSearch(
                stringResource(R.string.plants_title),
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    viewModel.onEvent(PlantsListEvent.OnSearchQueryChange(it))
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
                        PlantEditDestination(-1)
                    )
                },
                icon = { Icon(Icons.Rounded.Add, stringResource(R.string.add_plant_button)) },
                text = { Text(text = stringResource(R.string.add_plant_button)) },
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
                if (state.plants.any { it.tags?.contains(PlantTags.BROADLEAF_PLANT) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.broadleaf_title)
                        )
                    }
                    items(
                        state.plants.filter {
                            it.tags?.contains(PlantTags.BROADLEAF_PLANT) ?: false
                        }
                    ) { plant ->
                        GridItem(
                            label = plant.name,
                            message = plant.status ?: "",
                            imageUrl = plant.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(
                                        PlantEditDestination(
                                            plant.id!!
                                        )
                                    )
                                }
                        )
                    }
                }
                if (state.plants.any {
                        it.tags?.contains(PlantTags.LONG_STEMMED_PLANT) == true
                    }
                ) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.long_stemmed_title)
                        )
                    }
                    items(
                        state.plants.filter {
                            it.tags?.contains(PlantTags.LONG_STEMMED_PLANT) ?: false
                        }
                    ) { plant ->
                        GridItem(
                            label = plant.name,
                            message = plant.status ?: "",
                            imageUrl = plant.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(
                                        PlantEditDestination(
                                            plant.id!!
                                        )
                                    )
                                }
                        )
                    }
                }
                if (state.plants.any { it.tags?.contains(PlantTags.FLOATING_PLANT) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.floating_plant_title)
                        )
                    }
                    items(
                        state.plants.filter {
                            it.tags?.contains(PlantTags.FLOATING_PLANT) ?: false
                        }
                    ) { plant ->
                        GridItem(
                            label = plant.name,
                            message = plant.status ?: "",
                            imageUrl = plant.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(
                                        PlantEditDestination(
                                            plant.id!!
                                        )
                                    )
                                }
                        )
                    }
                }
                if (state.plants.any { it.tags?.contains(PlantTags.MOSS) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.moss_title)
                        )
                    }
                    items(
                        state.plants.filter {
                            it.tags?.contains(PlantTags.MOSS) ?: false
                        }
                    ) { plant ->
                        GridItem(
                            label = plant.name,
                            message = plant.status ?: "",
                            imageUrl = plant.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(
                                        PlantEditDestination(
                                            plant.id!!
                                        )
                                    )
                                }
                        )
                    }
                }
                if (state.plants.any { it.tags?.contains(PlantTags.FERN) == true }) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        GridTitle(
                            title = stringResource(R.string.fern_title)
                        )
                    }
                    items(
                        state.plants.filter {
                            it.tags?.contains(PlantTags.FERN) ?: false
                        }
                    ) { plant ->
                        GridItem(
                            label = plant.name,
                            message = plant.status ?: "",
                            imageUrl = plant.imageUrl,
                            modifier = Modifier
                                .clickable {
                                    navigator.navigate(
                                        PlantEditDestination(
                                            plant.id!!
                                        )
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}
