package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.bowfront

sealed interface BowfrontCalculatorEvent {

    object CalculateButtonPressed : BowfrontCalculatorEvent

    data class HeightChanged(val height: String) : BowfrontCalculatorEvent

    data class WidthChanged(val width: String) : BowfrontCalculatorEvent

    data class FullWidthChanged(val fullWidth: String) : BowfrontCalculatorEvent

    data class LengthChanged(val length: String) : BowfrontCalculatorEvent
}
