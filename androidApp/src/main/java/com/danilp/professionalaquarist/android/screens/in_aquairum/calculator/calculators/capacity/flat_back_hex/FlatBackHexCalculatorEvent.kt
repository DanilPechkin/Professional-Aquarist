package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.flat_back_hex

sealed interface FlatBackHexCalculatorEvent {

    object CalculateButtonPressed : FlatBackHexCalculatorEvent

    data class HeightChanged(val height: String) : FlatBackHexCalculatorEvent

    data class WidthChanged(val width: String) : FlatBackHexCalculatorEvent

    data class FullWidthChanged(val fullWidth: String) : FlatBackHexCalculatorEvent

    data class LengthChanged(val length: String) : FlatBackHexCalculatorEvent

    data class FullLengthChanged(val fullLength: String) : FlatBackHexCalculatorEvent
}
