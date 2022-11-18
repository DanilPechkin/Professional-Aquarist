package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.alkalinity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.android.navigation.nav_graphs.InAquariumNavGraph
import com.danilp.professionalaquarist.android.screens.destinations.CalculatorsDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.danilp.professionalaquarist.android.ui.AquariumTopBar
import com.danilp.professionalaquarist.android.ui.InfoFieldWithErrorAndIcon
import com.danilp.professionalaquarist.android.ui.OutlinedDropDownMenuField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun AlkalinityCalculator(
    navigator: DestinationsNavigator,
    viewModel: AlkalinityCalculatorViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isTopMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AquariumTopBar(
                title = stringResource(R.string.alkalinity_title),
                switchMenuVisibility = { isTopMenuExpanded = !isTopMenuExpanded },
                isMenuExpanded = isTopMenuExpanded,
                hideMenu = { isTopMenuExpanded = false },
                navigateBack = { navigator.navigate(CalculatorsDestination) },
                navigateToSettings = { navigator.navigate(SettingsScreenDestination()) },
                navigateToAccount = { }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            val focusManager = LocalFocusManager.current

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoFieldWithErrorAndIcon(
                    value = state.inputAlkalinity,
                    onValueChange = {
                        viewModel.onEvent(AlkalinityCalculatorEvent.InputAlkalinityChanged(it))
                    },
                    label = stringResource(R.string.input_alkalinity_label),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.clearFocus()
                        }
                    ),
                    errorCode = state.inputAlkalinityErrorCode,
                    maxLines = 1,
                    singleLine = true,
                    textFieldModifier = Modifier.fillMaxWidth(),
                    modifier = Modifier
                        .weight(2f)
                        .padding(end = 16.dp)
                )
                OutlinedDropDownMenuField(
                    label = stringResource(R.string.measure_label),
                    items = state.alkalinityMeasuresList,
                    selectedItem = state.alkalinityMeasuresList[state.inputAlkalinityMeasureCode],
                    changeSelectedItem = {
                        viewModel.onEvent(
                            AlkalinityCalculatorEvent.InputAlkalinityMeasureCodeChanged(
                                state.alkalinityMeasuresList.indexOf(it)
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.outputAlkalinity,
                    onValueChange = { },
                    label = { Text(text = stringResource(R.string.output_alkalinity_label)) },
                    readOnly = true,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(2f)
                )

                OutlinedDropDownMenuField(
                    label = stringResource(R.string.measure_label),
                    items = state.alkalinityMeasuresList,
                    selectedItem = state.alkalinityMeasuresList[state.outputAlkalinityMeasureCode],
                    changeSelectedItem = {
                        viewModel.onEvent(
                            AlkalinityCalculatorEvent.OutputAlkalinityMeasureCodeChanged(
                                state.alkalinityMeasuresList.indexOf(it)
                            )
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.onEvent(AlkalinityCalculatorEvent.ConvertButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.convert_button))
                }
            }
        }
    }
}
