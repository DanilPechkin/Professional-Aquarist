package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.info

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.professionalaquarist.android.screens.destinations.DwellerEditDestination
import com.danilp.professionalaquarist.android.screens.destinations.DwellersListDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.domain.aquarium.ComfortTags
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerStatusTags
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerTags
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@InAquariumNavGraph
@Destination
fun DwellerInfo(
    dwellerId: Long,
    navigator: DestinationsNavigator,
    viewModel: DwellerInfoViewModel = hiltViewModel()
) {

    val state = viewModel.state

    var isAdvancedExpanded by rememberSaveable { mutableStateOf(false) }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AquariumTopBar(
                stringResource(R.string.dweller_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigate(DwellersListDestination) },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.edit_button)) },
                icon = { Icon(Icons.Rounded.Edit, stringResource(R.string.edit_button)) },
                onClick = {
                    navigator.navigate(DwellerEditDestination(dwellerId = state.dweller.id ?: -1))
                },
                expanded = scrollState.value == 0
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            GlideImage(
                imageModel = state.dweller.imageUrl ?: R.drawable.aquairum_pic,
                contentDescription = stringResource(R.string.dweller_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.name_label)}: ${state.dweller.name}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.genus_label)}: ${state.dweller.genus}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.amount_label)}: ${state.dweller.amount}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.status_label)}: ${
                    when (state.dweller.status) {
                        ComfortTags.VERY_SATISFIED -> stringResource(R.string.very_satisfied)
                        ComfortTags.SATISFIED -> stringResource(R.string.satisfied)
                        ComfortTags.NEUTRAL -> stringResource(R.string.neutral_label)
                        ComfortTags.DISSATISFIED -> stringResource(R.string.dissatisfied)
                        ComfortTags.VERY_DISSATISFIED -> stringResource(R.string.very_dissatisfied)
                        else -> stringResource(R.string.satisfied)
                    }
                }"
            )

            if (state.dweller.statusTags?.isNotEmpty() == true) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${stringResource(R.string.cause_discomfort_label)}: ${
                        state.dweller.statusTags!!.map { tag ->
                            when (tag) {
                                DwellerStatusTags.WATER_PARAMETERS_NOT_MET ->
                                    stringResource(R.string.inappropriate_water_parameters)

                                DwellerStatusTags.AQUARIUM_CAPACITY_NOT_MET ->
                                    stringResource(R.string.fish_are_crowded_in_the_aquarium)

                                DwellerStatusTags.IN_DANGER ->
                                    stringResource(R.string.this_fish_is_in_danger)

                                DwellerStatusTags.TAGS_NOT_MET ->
                                    stringResource(R.string.wishes_of_this_fish_are_not_fulfilled)

                                else -> null
                            }
                        }.mapNotNull { it }.joinToString(", ")
                    }"
                )
            }

            if (state.dweller.statusTags?.contains(DwellerStatusTags.TAGS_NOT_MET) == true) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${stringResource(R.string.unfulfilled_wishes_label)}: ${
                        state.dweller.statusTags!!.map { tag ->
                            when (tag) {
                                DwellerTags.FAST_CURRENT ->
                                    stringResource(R.string.needs_fast_current_label)

                                DwellerTags.SLOW_CURRENT ->
                                    stringResource(R.string.needs_slow_current_label)

                                DwellerTags.MEDIUM_CURRENT ->
                                    stringResource(R.string.needs_medium_current_label)

                                DwellerTags.BRIGHT_LIGHT ->
                                    stringResource(R.string.needs_bright_light_label)

                                DwellerTags.LOW_LIGHT ->
                                    stringResource(R.string.needs_low_light_label)

                                DwellerTags.PLANT_LOVER ->
                                    stringResource(R.string.needs_lot_of_plants_label)

                                DwellerTags.NEEDS_SHELTER ->
                                    stringResource(R.string.needs_shelter_label)

                                DwellerTags.BROADLEAF_PLANT ->
                                    stringResource(R.string.needs_broadleaf_plant_label)

                                DwellerTags.LONG_STEMMED_PLANT ->
                                    stringResource(R.string.needs_long_stemmed_label)

                                DwellerTags.FLOATING_PLANT ->
                                    stringResource(R.string.needs_floating_plant_label)

                                DwellerTags.MOSS ->
                                    stringResource(R.string.needs_moss_label)

                                else -> null
                            }
                        }.mapNotNull { it }.joinToString(", ")
                    }"
                )
            }

            if (state.dweller.description != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${stringResource(R.string.description_label)}: ${state.dweller.description}"
                )
            }

            TextButton(
                onClick = { isAdvancedExpanded = !isAdvancedExpanded }
            ) {
                Text(text = stringResource(R.string.show_more_button))
                Icon(
                    imageVector =
                    if (isAdvancedExpanded)
                        Icons.Default.ExpandLess
                    else
                        Icons.Default.ExpandMore,
                    contentDescription = stringResource(R.string.show_more_button)
                )
            }

            AnimatedVisibility(
                visible = isAdvancedExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {

                    if (state.dweller.liters != null) {
                        Text(
                            text = "${stringResource(R.string.occupied_volume_in_the_aquarium)}: ${
                                state.dweller.liters!! * state.dweller.amount!!
                            }"
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(
                        text = "${stringResource(R.string.temperature_label)}: ${
                            state.dweller.minTemperature ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.dweller.maxTemperature ?: stringResource(id = R.string.unknown_label)
                        }"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.ph_label)}: ${
                            state.dweller.minPh ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.dweller.maxPh ?: stringResource(id = R.string.unknown_label)
                        }"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.gh_label)}: ${
                            state.dweller.minGh ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.dweller.maxGh ?: stringResource(id = R.string.unknown_label)
                        }"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.kh_label)}: ${
                            state.dweller.minKh ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.dweller.maxKh ?: stringResource(id = R.string.unknown_label)
                        }"
                    )
                }
            }
        }
    }
}