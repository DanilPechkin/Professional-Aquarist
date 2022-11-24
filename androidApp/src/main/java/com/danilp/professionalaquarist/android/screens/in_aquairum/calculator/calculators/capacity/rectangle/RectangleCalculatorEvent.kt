package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.rectangle

sealed interface RectangleCalculatorEvent {

    object CalculateButtonPressed : RectangleCalculatorEvent

    object ApplyButtonPressed : RectangleCalculatorEvent

    data class LengthChanged(val length: String) : RectangleCalculatorEvent

    data class WidthChanged(val width: String) : RectangleCalculatorEvent

    data class HeightChanged(val height: String) : RectangleCalculatorEvent
}
