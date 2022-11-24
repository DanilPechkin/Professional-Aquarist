package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.elliptical_cylinder

sealed interface EllipticalCylinderCalculatorEvent {

    object CalculateButtonPressed : EllipticalCylinderCalculatorEvent

    object ApplyButtonPressed : EllipticalCylinderCalculatorEvent

    data class HeightChanged(val height: String) : EllipticalCylinderCalculatorEvent

    data class WidthChanged(val width: String) : EllipticalCylinderCalculatorEvent

    data class LengthChanged(val length: String) : EllipticalCylinderCalculatorEvent
}
