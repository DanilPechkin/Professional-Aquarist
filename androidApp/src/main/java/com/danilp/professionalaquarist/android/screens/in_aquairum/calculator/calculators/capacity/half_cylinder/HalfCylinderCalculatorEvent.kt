package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.half_cylinder

sealed interface HalfCylinderCalculatorEvent {

    object CalculateButtonPressed : HalfCylinderCalculatorEvent

    data class HeightChanged(val height: String) : HalfCylinderCalculatorEvent

    data class DiameterChanged(val diameter: String) : HalfCylinderCalculatorEvent
}
