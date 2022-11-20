package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.info

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
import com.danilp.professionalaquarist.android.screens.destinations.PlantEditDestination
import com.danilp.professionalaquarist.android.screens.destinations.PlantsListDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.domain.aquarium.ComfortTags
import com.danilp.professionalaquarist.domain.plant.tags.PlantStatusTags
import com.danilp.professionalaquarist.domain.plant.tags.PlantTags
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@InAquariumNavGraph
@Destination
fun PlantInfo(
    plantId: Long,
    navigator: DestinationsNavigator,
    viewModel: PlantInfoViewModel = hiltViewModel()
) {

    val state = viewModel.state

    var isAdvancedExpanded by rememberSaveable { mutableStateOf(false) }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AquariumTopBar(
                stringResource(R.string.plant_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigate(PlantsListDestination) },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.edit_button)) },
                icon = { Icon(Icons.Rounded.Edit, stringResource(R.string.edit_button)) },
                onClick = {
                    navigator.navigate(PlantEditDestination(plantId = state.plant.id ?: -1))
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
                imageModel = state.plant.imageUrl ?: R.drawable.aquairum_pic,
                contentDescription = stringResource(R.string.plant_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.name_label)}: ${state.plant.name}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.genus_label)}: ${state.plant.genus}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.status_label)}: ${
                    when (state.plant.status) {
                        ComfortTags.VERY_SATISFIED.code -> stringResource(R.string.very_satisfied)
                        ComfortTags.SATISFIED.code -> stringResource(R.string.satisfied)
                        ComfortTags.NEUTRAL.code -> stringResource(R.string.neutral_label)
                        ComfortTags.DISSATISFIED.code -> stringResource(R.string.dissatisfied)
                        ComfortTags.VERY_DISSATISFIED.code -> stringResource(R.string.very_dissatisfied)
                        else -> stringResource(R.string.satisfied)
                    }
                }"
            )

            if (state.plant.statusTags?.isNotEmpty() == true) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${stringResource(R.string.cause_discomfort_label)}: ${
                        state.plant.statusTags!!.map { tag ->
                            when (tag) {
                                PlantStatusTags.WATER_PARAMETERS_NOT_MET.code ->
                                    stringResource(R.string.inappropriate_water_parameters)

                                PlantStatusTags.NOT_ENOUGH_ILLUMINATION.code ->
                                    stringResource(R.string.not_enough_illumination)

                                PlantStatusTags.IN_DANGER.code ->
                                    stringResource(R.string.this_plant_is_in_danger)

                                PlantStatusTags.TAGS_NOT_MET.code ->
                                    stringResource(R.string.wishes_of_this_plant_are_not_fulfilled)

                                else -> null
                            }
                        }.mapNotNull { it }.joinToString(", ")
                    }"
                )
            }

            if (state.plant.statusTags?.contains(PlantStatusTags.TAGS_NOT_MET.code) == true) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${stringResource(R.string.unfulfilled_wishes_label)}: ${
                        state.plant.statusTags!!.map { tag ->
                            when (tag) {
                                PlantTags.BRIGHT_LIGHT.code ->
                                    stringResource(R.string.needs_bright_light)

                                PlantTags.LOW_LIGHT.code ->
                                    stringResource(R.string.needs_low_light)

                                else -> null
                            }
                        }.mapNotNull { it }.joinToString(", ")
                    }"
                )
            }

            if (state.plant.description != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${stringResource(R.string.description_label)}: ${
                        state.plant.description
                    }"
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

                    Text(
                        text = "${stringResource(R.string.temperature_label)}: ${
                            state.plant.minTemperature ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.plant.maxTemperature ?: stringResource(id = R.string.unknown_label)
                        }"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.ph_label)}: ${
                            state.plant.minPh ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.plant.maxPh ?: stringResource(id = R.string.unknown_label)
                        }"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.gh_label)}: ${
                            state.plant.minGh ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.plant.maxGh ?: stringResource(id = R.string.unknown_label)
                        }"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.kh_label)}: ${
                            state.plant.minKh ?: stringResource(id = R.string.unknown_label)
                        }-${
                            state.plant.maxKh ?: stringResource(id = R.string.unknown_label)
                        }"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "${stringResource(R.string.co2_label)}: ${
                            state.plant.minCO2 ?: stringResource(id = R.string.unknown_label)
                        }"
                    )
                }
            }
        }
    }
}
