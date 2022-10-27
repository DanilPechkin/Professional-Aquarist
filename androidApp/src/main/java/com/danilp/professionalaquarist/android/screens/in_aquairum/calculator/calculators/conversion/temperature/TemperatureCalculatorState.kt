package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.temperature

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class TemperatureCalculatorState(
    val inputTemperature: String = "",
    val inputTemperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val inputTemperatureErrorCode: Int? = null,
    val outputTemperature: String = "",
    val outputTemperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val temperatureMeasuresList: List<String> = listOf()
)
