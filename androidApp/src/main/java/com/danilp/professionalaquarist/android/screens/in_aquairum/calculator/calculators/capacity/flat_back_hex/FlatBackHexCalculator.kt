package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.flat_back_hex

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.focus.FocusDirection
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
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun FlatBackHexCalculator(
    navigator: DestinationsNavigator,
    viewModel: FlatBackHexCalculatorViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isTopMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AquariumTopBar(
                title = stringResource(R.string.flatbackhex_title),
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
                .verticalScroll(rememberScrollState())
        ) {

            val focusManager = LocalFocusManager.current

            InfoFieldWithErrorAndIcon(
                value = state.height,
                onValueChange = {
                    viewModel.onEvent(FlatBackHexCalculatorEvent.HeightChanged(it))
                },
                label = stringResource(R.string.height_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.heightErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.width,
                onValueChange = {
                    viewModel.onEvent(FlatBackHexCalculatorEvent.WidthChanged(it))
                },
                label = stringResource(R.string.width_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.widthErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.fullWidth,
                onValueChange = {
                    viewModel.onEvent(FlatBackHexCalculatorEvent.FullWidthChanged(it))
                },
                label = stringResource(R.string.full_width_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.fullWidthErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.length,
                onValueChange = {
                    viewModel.onEvent(FlatBackHexCalculatorEvent.LengthChanged(it))
                },
                label = stringResource(R.string.length_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.lengthErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.fullLength,
                onValueChange = {
                    viewModel.onEvent(FlatBackHexCalculatorEvent.FullLengthChanged(it))
                },
                label = stringResource(R.string.full_length_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                ),
                errorCode = state.fullLengthErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.outputCapacity,
                onValueChange = { },
                label = { Text(text = stringResource(R.string.capacity_title)) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Text(
                        text = when (state.capacityMeasureCode) {
                            CapacityMeasure.Liters.code ->
                                stringResource(R.string.capacity_measure_liters)

                            CapacityMeasure.Gallons.code ->
                                stringResource(R.string.capacity_measure_gallons)

                            CapacityMeasure.CubicFeet.code ->
                                stringResource(R.string.capacity_measure_cubic_feet)

                            CapacityMeasure.USCups.code ->
                                stringResource(R.string.capacity_measure_us_cups)

                            CapacityMeasure.Teaspoons.code ->
                                stringResource(R.string.capacity_measure_teaspoons)

                            CapacityMeasure.Tablespoons.code ->
                                stringResource(R.string.capacity_measure_tablespoons)

                            CapacityMeasure.Milliliters.code ->
                                stringResource(R.string.capacity_measure_milliliters)

                            CapacityMeasure.MetricCups.code ->
                                stringResource(R.string.capacity_measure_metric_cups)

                            CapacityMeasure.CubicMeters.code ->
                                stringResource(R.string.capacity_measure_cubic_meters)

                            CapacityMeasure.CubicInches.code ->
                                stringResource(R.string.capacity_measure_cubic_inches)

                            CapacityMeasure.CubicCentimeters.code ->
                                stringResource(R.string.capacity_measure_cubic_centimeters)

                            else ->
                                ""
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                AnimatedVisibility(
                    visible = state.outputCapacity.isNotEmpty()
                ) {
                    Button(
                        onClick = {
                            viewModel.onEvent(FlatBackHexCalculatorEvent.ApplyButtonPressed)
                        }
                    ) {
                        Text(text = stringResource(R.string.apply_for_aquarium_button))
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        viewModel.onEvent(FlatBackHexCalculatorEvent.CalculateButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.calculate_button))
                }
            }
        }
    }
}
