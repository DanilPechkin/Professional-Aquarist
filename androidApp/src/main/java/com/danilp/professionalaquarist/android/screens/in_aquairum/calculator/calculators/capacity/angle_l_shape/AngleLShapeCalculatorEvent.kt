package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.angle_l_shape

sealed interface AngleLShapeCalculatorEvent {

    object CalculateButtonPressed : AngleLShapeCalculatorEvent

    object ApplyButtonPressed : AngleLShapeCalculatorEvent

    data class HeightChanged(val height: String) : AngleLShapeCalculatorEvent

    data class WidthChanged(val width: String) : AngleLShapeCalculatorEvent

    data class FullWidthChanged(val fullWidth: String) : AngleLShapeCalculatorEvent

    data class LengthChanged(val length: String) : AngleLShapeCalculatorEvent

    data class FullLengthChanged(val fullLength: String) : AngleLShapeCalculatorEvent

    data class LengthBetweenSideChanged(val length: String) : AngleLShapeCalculatorEvent

    data class WidthBetweenSideChanged(val width: String) : AngleLShapeCalculatorEvent
}
