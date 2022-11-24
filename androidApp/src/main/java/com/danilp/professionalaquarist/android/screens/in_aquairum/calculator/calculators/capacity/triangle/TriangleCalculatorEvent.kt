package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.triangle

sealed interface TriangleCalculatorEvent {

    object CalculateButtonPressed : TriangleCalculatorEvent

    object ApplyButtonPressed : TriangleCalculatorEvent

    data class HeightChanged(val height: String) : TriangleCalculatorEvent

    data class Side1Changed(val side1: String) : TriangleCalculatorEvent

    data class Side2Changed(val side2: String) : TriangleCalculatorEvent

    data class Side3Changed(val side3: String) : TriangleCalculatorEvent
}
