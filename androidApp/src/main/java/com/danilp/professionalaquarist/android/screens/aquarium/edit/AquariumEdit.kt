package com.danilp.professionalaquarist.android.screens.aquarium.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Save
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
import com.danilp.professionalaquarist.android.screens.destinations.AquariumListDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.android.ui.FromToInfoFields
import com.danilp.professionalaquarist.android.ui.ImagePicker
import com.danilp.professionalaquarist.android.ui.InfoFieldWithErrorAndIcon
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph
@Destination
@Composable
fun AquariumEdit(
    aquariumId: Long,
    navigator: DestinationsNavigator,
    viewModel: AquariumEditViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isAdvancedExpanded by rememberSaveable { mutableStateOf(false) }
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var openDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is AquariumEditViewModel.ValidationEvent.Success -> {
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
        Column(
            Modifier
                .padding(paddingValues)
                .padding(top = 0.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
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
                singleLine = true
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
                capacityMeasureCode = state.capacityMeasureCode
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
                )
            )

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

            AnimatedVisibility(
                visible = isAdvancedExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    FromToInfoFields(
                        label = stringResource(R.string.temperature_label),
                        valueFrom = state.minTemperature,
                        valueTo = state.maxTemperature,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinTemperatureChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxTemperatureChanged(it))
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
                            viewModel.onEvent(AquariumEditEvent.MinPhChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxPhChanged(it))
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
                            viewModel.onEvent(AquariumEditEvent.MinGhChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxGhChanged(it))
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
                            viewModel.onEvent(AquariumEditEvent.MinKhChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxKhChanged(it))
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
                        errorCodeFrom = state.minKhErrorCode,
                        errorCodeTo = state.maxKhErrorCode,
                        alkalinityMeasureCode = state.alkalinityMeasureCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.k_label),
                        valueFrom = state.minK,
                        valueTo = state.maxK,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinKChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxKChanged(it))
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
                        errorCodeFrom = state.minKErrorCode,
                        errorCodeTo = state.maxKErrorCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.ca_label),
                        valueFrom = state.minCa,
                        valueTo = state.maxCa,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinCaChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxCaChanged(it))
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
                        errorCodeFrom = state.minCaErrorCode,
                        errorCodeTo = state.maxCaErrorCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.fe_label),
                        valueFrom = state.minFe,
                        valueTo = state.maxFe,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinFeChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxFeChanged(it))
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
                        errorCodeFrom = state.minFeErrorCode,
                        errorCodeTo = state.maxFeErrorCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.mg_label),
                        valueFrom = state.minMg,
                        valueTo = state.maxMg,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinMgChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxMgChanged(it))
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
                        errorCodeFrom = state.minMgErrorCode,
                        errorCodeTo = state.maxMgErrorCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.ammonia_label),
                        valueFrom = state.minAmmonia,
                        valueTo = state.maxAmmonia,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinAmmoniaChanged(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxAmmoniaChanged(it))
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
                        errorCodeFrom = state.minAmmoniaErrorCode,
                        errorCodeTo = state.maxAmmoniaErrorCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.po4_label),
                        valueFrom = state.minPO4,
                        valueTo = state.maxPO4,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinPO4Changed(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxPO4Changed(it))
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
                        errorCodeFrom = state.minPO4ErrorCode,
                        errorCodeTo = state.maxPO4ErrorCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FromToInfoFields(
                        label = stringResource(R.string.no3_label),
                        valueFrom = state.minNO3,
                        valueTo = state.maxNO3,
                        onValueFromChange = {
                            viewModel.onEvent(AquariumEditEvent.MinNO3Changed(it))
                        },
                        onValueToChange = {
                            viewModel.onEvent(AquariumEditEvent.MaxNO3Changed(it))
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
                        errorCodeFrom = state.minNO3ErrorCode,
                        errorCodeTo = state.maxNO3ErrorCode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    InfoFieldWithErrorAndIcon(
                        value = state.minCO2,
                        onValueChange = { viewModel.onEvent(AquariumEditEvent.MinCO2Changed(it)) },
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
                            viewModel.onEvent(AquariumEditEvent.MinIlluminationChanged(it))
                        },
                        label = stringResource(R.string.illumination_label),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        errorCode = state.minIlluminationErrorCode,
                        maxLines = 1,
                        singleLine = true,
                        textFieldModifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
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
