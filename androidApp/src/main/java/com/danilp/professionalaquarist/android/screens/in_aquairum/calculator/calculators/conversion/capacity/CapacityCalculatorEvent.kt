package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.capacity

sealed interface CapacityCalculatorEvent {

    object ConvertButtonPressed : CapacityCalculatorEvent

    data class InputCapacityChanged(val capacity: String) : CapacityCalculatorEvent

    data class InputCapacityMeasureCodeChanged(val code: Int) : CapacityCalculatorEvent

    data class OutputCapacityMeasureCodeChanged(val code: Int) : CapacityCalculatorEvent
}
