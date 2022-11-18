package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.triangle

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
fun TriangleCalculator(
    navigator: DestinationsNavigator,
    viewModel: TriangleCalculatorViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isTopMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AquariumTopBar(
                title = stringResource(R.string.triangle_title),
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

            InfoFieldWithErrorAndIcon(
                value = state.height,
                onValueChange = {
                    viewModel.onEvent(TriangleCalculatorEvent.HeightChanged(it))
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
                value = state.side1,
                onValueChange = {
                    viewModel.onEvent(TriangleCalculatorEvent.Side1Changed(it))
                },
                label = stringResource(R.string.side1_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.side1ErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.side2,
                onValueChange = {
                    viewModel.onEvent(TriangleCalculatorEvent.Side2Changed(it))
                },
                label = stringResource(R.string.side2_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.side2ErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithErrorAndIcon(
                value = state.side3,
                onValueChange = {
                    viewModel.onEvent(TriangleCalculatorEvent.Side3Changed(it))
                },
                label = stringResource(R.string.side3_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                ),
                errorCode = state.side3ErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.outputCapacity,
                onValueChange = { },
                label = { Text(text = stringResource(R.string.capacity_label)) },
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
                Button(
                    onClick = {
                        viewModel.onEvent(TriangleCalculatorEvent.CalculateButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.calculate_button))
                }
            }
        }
    }
}
