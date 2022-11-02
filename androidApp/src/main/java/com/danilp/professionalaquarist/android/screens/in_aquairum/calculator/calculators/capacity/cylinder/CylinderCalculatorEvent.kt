package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.cylinder

sealed interface CylinderCalculatorEvent {

    object CalculateButtonPressed : CylinderCalculatorEvent

    data class HeightChanged(val height: String) : CylinderCalculatorEvent

    data class DiameterChanged(val diameter: String) : CylinderCalculatorEvent
}
