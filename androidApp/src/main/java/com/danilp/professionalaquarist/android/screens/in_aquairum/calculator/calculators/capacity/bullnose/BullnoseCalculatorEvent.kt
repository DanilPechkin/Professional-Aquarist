package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.bullnose

sealed interface BullnoseCalculatorEvent {

    object CalculateButtonPressed : BullnoseCalculatorEvent

    data class HeightChanged(val height: String) : BullnoseCalculatorEvent

    data class WidthChanged(val width: String) : BullnoseCalculatorEvent

    data class LengthChanged(val length: String) : BullnoseCalculatorEvent
}
