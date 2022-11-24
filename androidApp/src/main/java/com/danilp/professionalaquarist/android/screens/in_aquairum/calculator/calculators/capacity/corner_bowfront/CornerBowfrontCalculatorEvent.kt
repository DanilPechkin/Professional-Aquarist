package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.corner_bowfront

sealed interface CornerBowfrontCalculatorEvent {

    object CalculateButtonPressed : CornerBowfrontCalculatorEvent

    object ApplyButtonPressed : CornerBowfrontCalculatorEvent

    data class HeightChanged(val height: String) : CornerBowfrontCalculatorEvent

    data class WidthChanged(val width: String) : CornerBowfrontCalculatorEvent

    data class LengthChanged(val length: String) : CornerBowfrontCalculatorEvent
}
