package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.temperature

sealed interface TemperatureCalculatorEvent {

    object ConvertButtonPressed : TemperatureCalculatorEvent

    data class InputTemperatureChanged(val temperature: String) : TemperatureCalculatorEvent

    data class InputTemperatureMeasureCodeChanged(val code: Int) : TemperatureCalculatorEvent

    data class OutputTemperatureMeasureCodeChanged(val code: Int) : TemperatureCalculatorEvent
}
