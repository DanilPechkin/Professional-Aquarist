package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.regular_polygon

sealed interface RegularPolygonCalculatorEvent {

    object CalculateButtonPressed : RegularPolygonCalculatorEvent

    object ApplyButtonPressed : RegularPolygonCalculatorEvent

    data class HeightChanged(val height: String) : RegularPolygonCalculatorEvent

    data class SidesChanged(val sides: String) : RegularPolygonCalculatorEvent

    data class LengthChanged(val length: String) : RegularPolygonCalculatorEvent
}
