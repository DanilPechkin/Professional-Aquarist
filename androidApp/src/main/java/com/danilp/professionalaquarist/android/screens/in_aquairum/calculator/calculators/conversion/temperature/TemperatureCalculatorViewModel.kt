package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.temperature

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.ConvertCelsius
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class TemperatureCalculatorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val convertCelsius: ConvertCelsius,
    private val validate: Validate
) : ViewModel() {
    var state by mutableStateOf(TemperatureCalculatorState())

    init {
        state = state.copy(
            temperatureMeasuresList = listOf(
                context.getString(R.string.temp_measure_celsius),
                context.getString(R.string.temp_measure_fahrenheit),
                context.getString(R.string.temp_measure_kelvin)
            )
        )
    }

    fun onEvent(event: TemperatureCalculatorEvent) {
        when (event) {
            TemperatureCalculatorEvent.ConvertButtonPressed -> {
                calculateOutputTemperature()
            }

            is TemperatureCalculatorEvent.InputTemperatureChanged -> {
                state = state.copy(inputTemperature = event.temperature)
            }

            is TemperatureCalculatorEvent.InputTemperatureMeasureCodeChanged -> {
                state = state.copy(inputTemperatureMeasureCode = event.code)
            }

            is TemperatureCalculatorEvent.OutputTemperatureMeasureCodeChanged -> {
                state = state.copy(outputTemperatureMeasureCode = event.code)
            }
        }
    }

    private fun calculateOutputTemperature() {
        val inputTemperatureResult = validate.decimal(
            value = state.inputTemperature,
            isRequired = true
        )
        if (inputTemperatureResult.errorCode != null) {
            state = state.copy(inputTemperatureErrorCode = inputTemperatureResult.errorCode)
            return
        }

        val celsiusTemperature = convertCelsius.from(
            state.inputTemperatureMeasureCode,
            state.inputTemperature.toDouble()
        ).result
        val calculatedOutputTemperature = convertCelsius.to(
            state.outputTemperatureMeasureCode,
            celsiusTemperature
        ).result

        state = state.copy(
            outputTemperature = calculatedOutputTemperature.toString()
        )
    }
}
