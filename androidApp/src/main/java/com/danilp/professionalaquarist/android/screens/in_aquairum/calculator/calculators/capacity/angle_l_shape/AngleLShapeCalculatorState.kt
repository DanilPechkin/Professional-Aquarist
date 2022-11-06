package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.angle_l_shape

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure

data class AngleLShapeCalculatorState(
    val height: String = "",
    val width: String = "",
    val fullWidth: String = "",
    val length: String = "",
    val fullLength: String = "",
    val lengthBetweenSide: String = "",
    val widthBetweenSide: String = "",
    val outputCapacity: String = "",
    val heightErrorCode: Int? = null,
    val widthErrorCode: Int? = null,
    val fullWidthErrorCode: Int? = null,
    val lengthErrorCode: Int? = null,
    val fullLengthErrorCode: Int? = null,
    val lengthBetweenSideErrorCode: Int? = null,
    val widthBetweenSideErrorCode: Int? = null,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val metricMeasureCode: Int = MetricMeasure.Meters.code
)
