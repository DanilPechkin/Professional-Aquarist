package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.edit

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
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.ChildFriendly
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.FamilyRestroom
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.SetMeal
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
import com.danilp.professionalaquarist.android.screens.destinations.DwellersListDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.android.ui.FromToInfoFields
import com.danilp.professionalaquarist.android.ui.ImagePicker
import com.danilp.professionalaquarist.android.ui.InfoFieldWithError
import com.danilp.professionalaquarist.android.ui.InfoFieldWithErrorAndIcon
import com.danilp.professionalaquarist.android.ui.SelectChip
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerTags
import com.google.accompanist.flowlayout.FlowRow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun DwellerEdit(
    dwellerId: Long,
    navigator: DestinationsNavigator,
    viewModel: DwellerEditViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isAdvancedExpanded by rememberSaveable { mutableStateOf(false) }
    var isTagsExpanded by rememberSaveable { mutableStateOf(false) }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var openDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is DwellerEditViewModel.ValidationEvent.Success -> {
                    navigator.navigate(DwellersListDestination)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AquariumTopBar(
                stringResource(R.string.edit_dweller_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigate(DwellersListDestination) },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { },
                navigationIcon = Icons.Rounded.Close
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(R.string.save_button)) },
                icon = { Icon(Icons.Rounded.Save, stringResource(R.string.save_button)) },
                onClick = { viewModel.onEvent(DwellerEditEvent.InsertButtonPressed) },
                expanded = scrollState.value == 0
            )
        }
    ) { paddingValues ->
        if (openDeleteDialog) {
            AlertDialog(
                onDismissRequest = { openDeleteDialog = false },
                icon = { Icon(Icons.Rounded.Delete, stringResource(R.string.delete_button)) },
                title = { Text(text = stringResource(R.string.permanently_delete_title)) },
                text = { Text(text = stringResource(R.string.permanently_delete_dweller_text)) },
                dismissButton = {
                    TextButton(onClick = { openDeleteDialog = false }) {
                        Text(text = stringResource(R.string.cancel_button))
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.onEvent(DwellerEditEvent.DeleteButtonPressed) }
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
                imageUri = state.dweller.imageUrl,
                onSelection = { viewModel.onEvent(DwellerEditEvent.ImagePicked(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoFieldWithErrorAndIcon(
                    value = state.name,
                    onValueChange = { viewModel.onEvent(DwellerEditEvent.NameChanged(it)) },
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
                    modifier = Modifier.weight(3f),
                    textFieldModifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp)
                )
                InfoFieldWithError(
                    value = state.amount,
                    onValueChange = { viewModel.onEvent(DwellerEditEvent.AmountChanged(it)) },
                    label = stringResource(R.string.amount_label),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Next)
                        }
                    ),
                    errorCode = state.amountErrorCode,
                    maxLines = 1,
                    singleLine = true,
                    textFieldModifier = Modifier.fillMaxWidth(),
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.genus,
                onValueChange = { viewModel.onEvent(DwellerEditEvent.GenusChanged(it)) },
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
                onValueChange = { viewModel.onEvent(DwellerEditEvent.DescriptionChanged(it)) },
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
                        imageVector = Icons.Rounded.SetMeal,
                        contentDescription = stringResource(R.string.dweller_title),
                        tint = if (state.typeTagErrorCode != null) MaterialTheme.colorScheme.error
                        else LocalContentColor.current,
                        modifier = Modifier
                            .padding(end = 8.dp)
                    )

                    Text(
                        text = stringResource(R.string.dweller_type_label),
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
                        DwellerTags.FISH.code,
                        DwellerTags.BIVALVE.code,
                        DwellerTags.CRAYFISH.code,
                        DwellerTags.SHRIMP.code,
                        DwellerTags.CRAB.code,
                        DwellerTags.SNAIL.code
                    ).forEach { tag ->
                        SelectChip(
                            selected = state.typeTag == tag,
                            onClick = { viewModel.onEvent(DwellerEditEvent.TypeTagSelected(tag)) },
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

            Row {
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

                    Column(
                        Modifier.padding(horizontal = 8.dp)
                    ) {
                        Row {
                            Icon(
                                imageVector = Icons.Rounded.Restaurant,
                                contentDescription = stringResource(R.string.food_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.diet_label)
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
                                DwellerTags.CARNIVOROUS.code,
                                DwellerTags.HERBIVOROUS.code,
                                DwellerTags.OMNIVOROUS.code
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                imageVector = Icons.Rounded.Psychology,
                                contentDescription = stringResource(R.string.behavior_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.behavior_title)
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
                                DwellerTags.PREDATOR.code,
                                DwellerTags.PEACEFUL.code,
                                DwellerTags.TERRITORIAL.code
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                imageVector = Icons.Rounded.Air,
                                contentDescription = stringResource(R.string.current_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.preffered_current_title)
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
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                DwellerTags.BRIGHT_LIGHT.code,
                                DwellerTags.LOW_LIGHT.code,
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                imageVector = Icons.Rounded.FamilyRestroom,
                                contentDescription = stringResource(R.string.family_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.family_title)
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
                                DwellerTags.MONOGAMOUS.code,
                                DwellerTags.POLYGAMOUS.code
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                imageVector = Icons.Rounded.ChildFriendly,
                                contentDescription = stringResource(R.string.childbirh_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.childbirh_title)
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
                                DwellerTags.LIVEBEARER.code,
                                DwellerTags.OVIPAROUS.code
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                imageVector = Icons.Rounded.AutoAwesome,
                                contentDescription =
                                stringResource(R.string.particular_qualities_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.particular_qualities_title)
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
                                DwellerTags.SHOAL.code,
                                DwellerTags.CLEANER.code,
                                DwellerTags.PLANT_EATER.code
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                imageVector = Icons.Rounded.Checklist,
                                contentDescription =
                                stringResource(R.string.wishes_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.wishes_title)
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
                                DwellerTags.PLANT_LOVER.code,
                                DwellerTags.NEEDS_SHELTER.code,
                                DwellerTags.NEEDS_DRIFTWOOD.code
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
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
                                imageVector = Icons.Rounded.Spa,
                                contentDescription =
                                stringResource(R.string.favorite_plants_title),
                                modifier = Modifier
                                    .padding(end = 8.dp)
                            )

                            Text(
                                text = stringResource(R.string.favorite_plants_title)
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
                                DwellerTags.BROADLEAF_PLANT.code,
                                DwellerTags.FLOATING_PLANT.code,
                                DwellerTags.MOSS.code,
                                DwellerTags.LONG_STEMMED_PLANT.code
                            ).forEach { tag ->
                                SelectChip(
                                    selected = state.tags.contains(tag),
                                    onClick = {
                                        viewModel.onEvent(DwellerEditEvent.TagSelected(tag))
                                    },
                                    labelCode = tag
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            AnimatedVisibility(
                visible = isAdvancedExpanded,
                enter = expandVertically(tween(durationMillis = 400)) + fadeIn(),
                exit = shrinkVertically(tween(durationMillis = 400)) + fadeOut()
            ) {
                Column {

                    InfoFieldWithErrorAndIcon(
                        value = state.liters,
                        onValueChange = {
                            viewModel.onEvent(DwellerEditEvent.LitersChanged(it))
                        },
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
                        textFieldModifier = Modifier.fillMaxWidth(),
                        capacityMeasureCode = state.capacityMeasureCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.temperature_label),
                        valueFrom = state.minTemperature,
                        valueTo = state.maxTemperature,
                        onValueFromChange = {
                            viewModel.onEvent(DwellerEditEvent.MinTemperatureChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(DwellerEditEvent.MaxTemperatureChanged(it))
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
                        onValueFromChange = {
                            viewModel.onEvent(DwellerEditEvent.MinPhChanged(it))
                        },
                        onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxPhChanged(it)) },
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
                        onValueFromChange = {
                            viewModel.onEvent(DwellerEditEvent.MinGhChanged(it))
                        },
                        onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxGhChanged(it)) },
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
                        onValueFromChange = {
                            viewModel.onEvent(DwellerEditEvent.MinKhChanged(it))
                        },
                        onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxKhChanged(it)) },
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
