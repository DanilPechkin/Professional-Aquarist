package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
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

    var isTopMenuExpanded by remember { mutableStateOf(false) }

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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            val focusManager = LocalFocusManager.current

            ImagePicker(
                imageUri = state.dweller.imageUri,
                onSelection = { viewModel.onEvent(DwellerEditEvent.ImagePicked(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 256.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
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
                textFieldModifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
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
                        .padding(end = 16.dp)
                )
                InfoFieldWithError(
                    value = state.liters,
                    onValueChange = { viewModel.onEvent(DwellerEditEvent.LitersChanged(it)) },
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
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.temperature_label),
                valueFrom = state.minTemperature,
                valueTo = state.maxTemperature,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinTemperatureChanged(it)) },
                onValueToChange = { viewModel.onEvent(DwellerEditEvent.MaxTemperatureChanged(it)) },
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
                errorCodeTo = state.maxTemperatureErrorCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.ph_label),
                valueFrom = state.minPh,
                valueTo = state.maxPh,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinPhChanged(it)) },
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
                errorCodeTo = state.maxPhErrorCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.gh_label),
                valueFrom = state.minGh,
                valueTo = state.maxGh,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinGhChanged(it)) },
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
                errorCodeTo = state.maxGhErrorCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            FromToInfoFields(
                label = stringResource(R.string.kh_label),
                valueFrom = state.minKh,
                valueTo = state.maxKh,
                onValueFromChange = { viewModel.onEvent(DwellerEditEvent.MinKhChanged(it)) },
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
                errorCodeTo = state.maxKhErrorCode
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

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(DwellerEditEvent.DeleteButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.delete_button))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        viewModel.onEvent(DwellerEditEvent.InsertButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.save_button))
                }
            }
        }
    }
}