package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.angle_l_shape

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
import com.danilp.professionalaquarist.android.screens.AquariumTopBar
import com.danilp.professionalaquarist.android.screens.InfoFieldWithError
import com.danilp.professionalaquarist.android.screens.destinations.CalculatorsDestination
import com.danilp.professionalaquarist.android.screens.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@InAquariumNavGraph
@Destination
@Composable
fun AngleLShapeCalculator(
    navigator: DestinationsNavigator,
    viewModel: AngleLShapeCalculatorViewModel = hiltViewModel()
) {
    val state = viewModel.state

    var isTopMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AquariumTopBar(
                title = stringResource(R.string.anglelshape_title),
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

            InfoFieldWithError(
                value = state.height,
                onValueChange = {
                    viewModel.onEvent(AngleLShapeCalculatorEvent.HeightChanged(it))
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

            InfoFieldWithError(
                value = state.width,
                onValueChange = {
                    viewModel.onEvent(AngleLShapeCalculatorEvent.WidthChanged(it))
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

            InfoFieldWithError(
                value = state.fullWidth,
                onValueChange = {
                    viewModel.onEvent(AngleLShapeCalculatorEvent.FullWidthChanged(it))
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

            InfoFieldWithError(
                value = state.length,
                onValueChange = {
                    viewModel.onEvent(AngleLShapeCalculatorEvent.LengthChanged(it))
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

            InfoFieldWithError(
                value = state.fullLength,
                onValueChange = {
                    viewModel.onEvent(AngleLShapeCalculatorEvent.FullLengthChanged(it))
                },
                label = stringResource(R.string.full_length_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.fullLengthErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.lengthBetweenSide,
                onValueChange = {
                    viewModel.onEvent(AngleLShapeCalculatorEvent.LengthBetweenSideChanged(it))
                },
                label = stringResource(R.string.length_between_side_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    }
                ),
                errorCode = state.lengthBetweenSideErrorCode,
                maxLines = 1,
                singleLine = true,
                textFieldModifier = Modifier.fillMaxWidth(),
                metricMeasureCode = state.metricMeasureCode
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoFieldWithError(
                value = state.widthBetweenSide,
                onValueChange = {
                    viewModel.onEvent(AngleLShapeCalculatorEvent.WidthBetweenSideChanged(it))
                },
                label = stringResource(R.string.width_between_side_label),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                ),
                errorCode = state.widthBetweenSideErrorCode,
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
                readOnly = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.onEvent(AngleLShapeCalculatorEvent.CalculateButtonPressed)
                    }
                ) {
                    Text(text = stringResource(R.string.calculate_button))
                }
            }
        }
    }
}
