package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.l_shape

sealed interface LShapeCalculatorEvent {

    object CalculateButtonPressed : LShapeCalculatorEvent

    data class HeightChanged(val height: String) : LShapeCalculatorEvent

    data class WidthChanged(val width: String) : LShapeCalculatorEvent

    data class FullWidthChanged(val fullWidth: String) : LShapeCalculatorEvent

    data class LengthChanged(val length: String) : LShapeCalculatorEvent

    data class FullLengthChanged(val fullLength: String) : LShapeCalculatorEvent
}
