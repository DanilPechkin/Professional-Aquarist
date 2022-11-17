package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
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
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import com.danilp.professionalaquarist.android.screens.AquariumTopBar
import com.danilp.professionalaquarist.android.screens.FromToInfoFields
import com.danilp.professionalaquarist.android.screens.ImagePicker
import com.danilp.professionalaquarist.android.screens.InfoFieldWithError
import com.danilp.professionalaquarist.android.screens.InfoFieldWithErrorAndIcon
import com.danilp.professionalaquarist.android.screens.destinations.DwellersListDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
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
    var isTopMenuExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
                navigateBack = { navigator.navigateUp() },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { }
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
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
            }

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(DwellerEditEvent.DeleteButtonPressed)
                    },
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
