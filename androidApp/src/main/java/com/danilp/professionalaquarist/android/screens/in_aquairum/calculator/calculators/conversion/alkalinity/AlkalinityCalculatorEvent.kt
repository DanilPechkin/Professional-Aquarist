package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.alkalinity

sealed interface AlkalinityCalculatorEvent {

    object ConvertButtonPressed : AlkalinityCalculatorEvent

    data class InputAlkalinityChanged(val alkalinity: String) : AlkalinityCalculatorEvent

    data class InputAlkalinityMeasureCodeChanged(val code: Int) : AlkalinityCalculatorEvent

    data class OutputAlkalinityMeasureCodeChanged(val code: Int) : AlkalinityCalculatorEvent
}
