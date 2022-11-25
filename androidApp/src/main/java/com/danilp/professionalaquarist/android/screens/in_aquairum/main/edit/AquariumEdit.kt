package com.danilp.professionalaquarist.android.screens.in_aquairum.main.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.Chair
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Tungsten
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.professionalaquarist.android.screens.destinations.AquariumListDestination
import com.danilp.professionalaquarist.android.screens.destinations.MainAquariumScreenDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumSelectChip
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.android.ui.GridTitle
import com.danilp.professionalaquarist.android.ui.ImagePicker
import com.danilp.professionalaquarist.android.ui.InfoFieldWithErrorAndIcon
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerTags
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun AquariumEdit(
    navigator: DestinationsNavigator,
    viewModel: AquariumEditViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isStatsExpanded by rememberSaveable { mutableStateOf(false) }
    var isTagsExpanded by rememberSaveable { mutableStateOf(false) }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var openDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.completionEvents.collect { event ->
            when (event) {
                is AquariumEditViewModel.CompletionEvent.Save -> {
                    navigator.navigate(MainAquariumScreenDestination)
                }

                AquariumEditViewModel.CompletionEvent.Delete -> {
                    navigator.navigate(AquariumListDestination)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AquariumTopBar(
                stringResource(R.string.edit_aquarium_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { },
                Icons.Rounded.Close
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.save_button)) },
                icon = { Icon(Icons.Rounded.Save, stringResource(R.string.save_button)) },
                onClick = { viewModel.onEvent(AquariumEditEvent.InsertButtonPressed) },
                expanded = scrollState.value == 0
            )
        }
    ) { paddingValues ->

        if (openDeleteDialog) {
            AlertDialog(
                onDismissRequest = { openDeleteDialog = false },
                icon = { Icon(Icons.Rounded.Delete, stringResource(R.string.delete_button)) },
                title = { Text(text = stringResource(R.string.permanently_delete_title)) },
                text = { Text(text = stringResource(R.string.permanently_delete_aquarium_text)) },
                dismissButton = {
                    TextButton(onClick = { openDeleteDialog = false }) {
                        Text(text = stringResource(R.string.cancel_button))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onEvent(AquariumEditEvent.DeleteButtonPressed) }
                    ) {
                        Text(text = stringResource(R.string.delete_button))
                    }
                }
            )
        }

        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
            )
        }

        Column(
            Modifier
                .padding(paddingValues)
                .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {

            val focusManager = LocalFocusManager.current

            ImagePicker(
                imageUri = state.aquarium.imageUrl,
                onSelection = { viewModel.onEvent(AquariumEditEvent.ImagePicked(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.name,
                onValueChange = { viewModel.onEvent(AquariumEditEvent.NameChanged(it)) },
                label = stringResource(R.string.name_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.nameErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.liters,
                onValueChange = { viewModel.onEvent(AquariumEditEvent.LitersChanged(it)) },
                label = stringResource(R.string.capacity_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.litersErrorCode,
                maxLines = 1,
                singleLine = true,
                capacityMeasureCode = state.capacityMeasureCode,
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(AquariumEditEvent.DescriptionChanged(it)) },
                label = {
                    Text(text = stringResource(R.string.description_label))
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                TextButton(
                    onClick = { isStatsExpanded = !isStatsExpanded }
                ) {
                    Text(text = stringResource(R.string.show_stats_button))
                    Icon(
                        imageVector =
                        if (isStatsExpanded)
                            Icons.Default.ExpandLess
                        else
                            Icons.Default.ExpandMore,
                        contentDescription = stringResource(R.string.show_stats_button)
                    )
                }

                TextButton(
                    onClick = { isTagsExpanded = !isTagsExpanded }
                ) {
                    Text(text = stringResource(R.string.show_tags_button))
                    Icon(
                        imageVector =
                        if (isTagsExpanded)
                            Icons.Default.ExpandLess
                        else
                            Icons.Default.ExpandMore,
                        contentDescription = stringResource(R.string.show_tags_button)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = isTagsExpanded,
                enter = expandVertically(tween(durationMillis = 400)) + fadeIn(),
                exit = shrinkVertically(tween(durationMillis = 400)) + fadeOut()
            ) {
                Column {

                    GridTitle(title = stringResource(R.string.required_tags_title))

                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        mainAxisSpacing = 8.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        state.requiredTags.forEach { tag ->
                            AquariumSelectChip(
                                selected = state.currentTags.contains(tag),
                                onClick = { },
                                labelCode = tag
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GridTitle(title = stringResource(R.string.current_tags_title))

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Air,
                                contentDescription = stringResource(R.string.current_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.aquarium_current_title)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            mainAxisSpacing = 8.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            listOf(
                                DwellerTags.FAST_CURRENT.code,
                                DwellerTags.MEDIUM_CURRENT.code,
                                DwellerTags.SLOW_CURRENT.code
                            ).forEach { tag ->
                                AquariumSelectChip(
                                    selected = state.currentTags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(AquariumEditEvent.CurrentTagSelected(tag))
                                    },
                                    labelCode = tag
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Tungsten,
                                contentDescription = stringResource(
                                    R.string.illumination_label
                                ),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.aquarium_lightning_label)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            mainAxisSpacing = 8.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            listOf(
                                DwellerTags.BRIGHT_LIGHT.code,
                                DwellerTags.LOW_LIGHT.code,
                            ).forEach { tag ->
                                AquariumSelectChip(
                                    selected = state.currentTags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(AquariumEditEvent.CurrentTagSelected(tag))
                                    },
                                    labelCode = tag
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Chair,
                                contentDescription = stringResource(
                                    R.string.aquarium_decor_title
                                ),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.aquarium_decor_title)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            mainAxisSpacing = 8.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            listOf(
                                DwellerTags.VEIL_TAILED.code,
                                DwellerTags.NEEDS_SHELTER.code,
                                DwellerTags.NEEDS_DRIFTWOOD.code
                            ).forEach { tag ->
                                AquariumSelectChip(
                                    selected = state.currentTags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(AquariumEditEvent.CurrentTagSelected(tag))
                                    },
                                    labelCode = tag
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            AnimatedVisibility(
                visible = isStatsExpanded,
                enter = expandVertically(tween(durationMillis = 400)) + fadeIn(),
                exit = shrinkVertically(tween(durationMillis = 400)) + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {

                    if (
                        state.currentTemperature.isNotEmpty() ||
                        state.minTemperature.isNotEmpty() ||
                        state.maxTemperature.isNotEmpty()
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.current_temperature_title) + ": " +
                                    state.currentTemperature.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.temperatureMeasureCode) {
                                        TemperatureMeasure.Celsius.code ->
                                            stringResource(
                                                R.string.temperature_measure_celsius_short
                                            )

                                        TemperatureMeasure.Fahrenheit.code ->
                                            stringResource(
                                                R.string.temperature_measure_fahrenheit_short
                                            )

                                        TemperatureMeasure.Kelvin.code ->
                                            stringResource(
                                                R.string.temperature_measure_kelvin_short
                                            )

                                        else ->
                                            ""
                                    }
                            )
                            Text(
                                text = stringResource(R.string.ideal_temperature_title) + ": " +
                                    state.minTemperature.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.temperatureMeasureCode) {
                                        TemperatureMeasure.Celsius.code ->
                                            stringResource(
                                                R.string.temperature_measure_celsius_short
                                            )

                                        TemperatureMeasure.Fahrenheit.code ->
                                            stringResource(
                                                R.string.temperature_measure_fahrenheit_short
                                            )

                                        TemperatureMeasure.Kelvin.code ->
                                            stringResource(
                                                R.string.temperature_measure_kelvin_short
                                            )

                                        else ->
                                            ""
                                    } + " — " +
                                    state.maxTemperature.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.temperatureMeasureCode) {
                                        TemperatureMeasure.Celsius.code ->
                                            stringResource(
                                                R.string.temperature_measure_celsius_short
                                            )

                                        TemperatureMeasure.Fahrenheit.code ->
                                            stringResource(
                                                R.string.temperature_measure_fahrenheit_short
                                            )

                                        TemperatureMeasure.Kelvin.code ->
                                            stringResource(
                                                R.string.temperature_measure_kelvin_short
                                            )

                                        else -> ""
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (
                        state.currentPh.isNotEmpty() ||
                        state.minPh.isNotEmpty() ||
                        state.maxPh.isNotEmpty()
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.current_ph_title) + ": " +
                                    state.currentPh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    }
                            )
                            Text(
                                text = stringResource(R.string.ideal_ph_title) + ": " +
                                    state.minPh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    } + " — " +
                                    state.maxPh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (
                        state.currentGh.isNotEmpty() ||
                        state.minGh.isNotEmpty() ||
                        state.maxGh.isNotEmpty()
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.current_gh_title) + ": " +
                                    state.currentGh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    }
                            )
                            Text(
                                text = stringResource(R.string.ideal_gh_title) + ": " +
                                    state.minGh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    } + " — " +
                                    state.maxGh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    if (
                        state.currentKh.isNotEmpty() ||
                        state.minKh.isNotEmpty() ||
                        state.maxKh.isNotEmpty()
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.current_kh_title) + ": " +
                                    state.currentKh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    }
                            )
                            Text(
                                text = stringResource(R.string.ideal_kh_title) + ": " +
                                    state.minKh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    } + " — " +
                                    state.maxKh.ifEmpty {
                                        stringResource(R.string.unknown_label)
                                    } + " " +
                                    when (state.alkalinityMeasureCode) {
                                        AlkalinityMeasure.DKH.code ->
                                            stringResource(R.string.alkalinity_measure_dkh_short)

                                        AlkalinityMeasure.MEQL.code ->
                                            stringResource(R.string.alkalinity_measure_meql_short)

                                        AlkalinityMeasure.MGL.code ->
                                            stringResource(R.string.alkalinity_measure_mgl_short)

                                        AlkalinityMeasure.PPM.code ->
                                            stringResource(R.string.alkalinity_measure_ppm_short)

                                        else ->
                                            ""
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { openDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text(text = stringResource(R.string.delete_button))
                }
            }
        }
    }
}
