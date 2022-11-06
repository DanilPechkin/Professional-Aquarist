package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.trapezoid

sealed interface TrapezoidCalculatorEvent {

    object CalculateButtonPressed : TrapezoidCalculatorEvent

    data class HeightChanged(val height: String) : TrapezoidCalculatorEvent

    data class WidthChanged(val width: String) : TrapezoidCalculatorEvent

    data class FullWidthChanged(val fullWidth: String) : TrapezoidCalculatorEvent

    data class LengthChanged(val length: String) : TrapezoidCalculatorEvent
}
