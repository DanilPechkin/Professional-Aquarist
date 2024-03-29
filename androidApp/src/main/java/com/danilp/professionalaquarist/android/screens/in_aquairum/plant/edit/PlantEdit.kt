package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Tungsten
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.danilp.professionalaquarist.android.screens.destinations.PlantsListDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.android.ui.FromToInfoFields
import com.danilp.professionalaquarist.android.ui.ImagePicker
import com.danilp.professionalaquarist.android.ui.InfoFieldWithErrorAndIcon
import com.danilp.professionalaquarist.android.ui.SelectChip
import com.danilp.professionalaquarist.domain.plant.tags.PlantTags
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun PlantEdit(
    plantId: Long,
    navigator: DestinationsNavigator,
    viewModel: PlantEditViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isAdvancedExpanded by rememberSaveable { mutableStateOf(false) }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var openDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is PlantEditViewModel.ValidationEvent.Success -> {
                    navigator.navigate(PlantsListDestination)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AquariumTopBar(
                stringResource(R.string.edit_plant_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigate(PlantsListDestination) },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { },
                Icons.Rounded.Close
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.save_button)) },
                icon = { Icon(Icons.Rounded.Save, stringResource(R.string.save_button)) },
                onClick = { viewModel.onEvent(PlantEditEvent.InsertButtonPressed) },
                expanded = scrollState.value == 0
            )
        }
    ) { paddingValues ->

        if (openDeleteDialog) {
            AlertDialog(
                onDismissRequest = { openDeleteDialog = false },
                icon = { Icon(Icons.Rounded.Delete, stringResource(R.string.delete_button)) },
                title = { Text(text = stringResource(R.string.permanently_delete_title)) },
                text = { Text(text = stringResource(R.string.permanently_delete_plant_text)) },
                dismissButton = {
                    TextButton(onClick = { openDeleteDialog = false }) {
                        Text(text = stringResource(R.string.cancel_button))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onEvent(PlantEditEvent.DeleteButtonPressed) }
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
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            val focusManager = LocalFocusManager.current

            ImagePicker(
                imageUri = state.plant.imageUrl,
                onSelection = { viewModel.onEvent(PlantEditEvent.ImagePicked(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.name,
                onValueChange = { viewModel.onEvent(PlantEditEvent.NameChanged(it)) },
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

            OutlinedTextField(
                value = state.genus,
                onValueChange = { viewModel.onEvent(PlantEditEvent.GenusChanged(it)) },
                label = {
                    Text(text = stringResource(R.string.genus_label))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                maxLines = 1,
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onEvent(PlantEditEvent.DescriptionChanged(it)) },
                label = {
                    Text(text = stringResource(R.string.description_label))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                Modifier.padding(horizontal = 8.dp)
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Spa,
                        contentDescription = stringResource(R.string.plant_title),
                        tint = if (state.typeTagErrorCode != null) MaterialTheme.colorScheme.error
                        else LocalContentColor.current,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = stringResource(R.string.plant_type_label),
                        color = if (state.typeTagErrorCode != null) MaterialTheme.colorScheme.error
                        else LocalContentColor.current,
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
                        PlantTags.BROADLEAF_PLANT.code,
                        PlantTags.MOSS.code,
                        PlantTags.FLOATING_PLANT.code,
                        PlantTags.LONG_STEMMED_PLANT.code,
                        PlantTags.FERN.code
                    ).forEach { tag ->
                        SelectChip(
                            selected = state.typeTag == tag,
                            onClick = { viewModel.onEvent(PlantEditEvent.TypeTagSelected(tag)) },
                            labelCode = tag,
                            isError = state.typeTagErrorCode != null
                        )
                    }
                }

                if (state.typeTagErrorCode != null) {
                    Text(
                        text = stringResource(R.string.choose_one_label),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { isAdvancedExpanded = !isAdvancedExpanded }
            ) {
                Text(text = stringResource(R.string.show_advanced_button))
                Icon(
                    imageVector =
                    if (isAdvancedExpanded)
                        Icons.Default.ExpandLess
                    else
                        Icons.Default.ExpandMore,
                    contentDescription = stringResource(R.string.show_advanced_button)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = isAdvancedExpanded,
                enter = expandVertically(tween(durationMillis = 400)) + fadeIn(),
                exit = shrinkVertically(tween(durationMillis = 400)) + fadeOut()
            ) {
                Column {

                    Column(
                        Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Tungsten,
                                contentDescription = stringResource(
                                    R.string.required_illumination_label
                                ),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.preffered_light_label)
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
                                PlantTags.LOW_LIGHT.code,
                                PlantTags.BRIGHT_LIGHT.code,
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(PlantEditEvent.TagSelected(tag))
                                    },
                                    labelCode = tag
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.temperature_title),
                        valueFrom = state.minTemperature,
                        valueTo = state.maxTemperature,
                        onValueFromChange = {
                            viewModel.onEvent(
                                PlantEditEvent.MinTemperatureChanged(
                                    it
                                )
                            )
                        },
                        onValueToChange = {
                            viewModel.onEvent(
                                PlantEditEvent.MaxTemperatureChanged(
                                    it
                                )
                            )
                        },
                        keyboardActionsFrom = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        keyboardActionsTo = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        errorCodeFrom = state.minTemperatureErrorCode,
                        errorCodeTo = state.maxTemperatureErrorCode,
                        temperatureMeasureCode = state.temperatureMeasureCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.ph_label),
                        valueFrom = state.minPh,
                        valueTo = state.maxPh,
                        onValueFromChange = { viewModel.onEvent(PlantEditEvent.MinPhChanged(it)) },
                        onValueToChange = { viewModel.onEvent(PlantEditEvent.MaxPhChanged(it)) },
                        keyboardActionsFrom = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        keyboardActionsTo = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        errorCodeFrom = state.minPhErrorCode,
                        errorCodeTo = state.maxPhErrorCode,
                        alkalinityMeasureCode = state.alkalinityMeasureCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.gh_label),
                        valueFrom = state.minGh,
                        valueTo = state.maxGh,
                        onValueFromChange = { viewModel.onEvent(PlantEditEvent.MinGhChanged(it)) },
                        onValueToChange = { viewModel.onEvent(PlantEditEvent.MaxGhChanged(it)) },
                        keyboardActionsFrom = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        keyboardActionsTo = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        errorCodeFrom = state.minGhErrorCode,
                        errorCodeTo = state.maxGhErrorCode,
                        alkalinityMeasureCode = state.alkalinityMeasureCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.kh_label),
                        valueFrom = state.minKh,
                        valueTo = state.maxKh,
                        onValueFromChange = { viewModel.onEvent(PlantEditEvent.MinKhChanged(it)) },
                        onValueToChange = { viewModel.onEvent(PlantEditEvent.MaxKhChanged(it)) },
                        keyboardActionsFrom = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        keyboardActionsTo = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        errorCodeFrom = state.minKhErrorCode,
                        errorCodeTo = state.maxKhErrorCode,
                        alkalinityMeasureCode = state.alkalinityMeasureCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoFieldWithErrorAndIcon(
                        value = state.minCO2,
                        onValueChange = { viewModel.onEvent(PlantEditEvent.MinCO2Changed(it)) },
                        label = stringResource(R.string.min_co2_label),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        errorCode = state.minCO2ErrorCode,
                        maxLines = 1,
                        singleLine = true,
                        textFieldModifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoFieldWithErrorAndIcon(
                        value = state.minIllumination,
                        onValueChange = {
                            viewModel.onEvent(PlantEditEvent.MinIlluminationChanged(it))
                        },
                        label = stringResource(R.string.required_illumination_label),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Next)
                            }
                        ),
                        errorCode = state.minIlluminationErrorCode,
                        maxLines = 1,
                        singleLine = true,
                        textFieldModifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
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
