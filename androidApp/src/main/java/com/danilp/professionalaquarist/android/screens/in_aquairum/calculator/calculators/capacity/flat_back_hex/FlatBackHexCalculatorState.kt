package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.flat_back_hex

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure

data class FlatBackHexCalculatorState(
    val height: String = "",
    val width: String = "",
    val fullWidth: String = "",
    val length: String = "",
    val fullLength: String = "",
    val outputCapacity: String = "",
    val heightErrorCode: Int? = null,
    val widthErrorCode: Int? = null,
    val fullWidthErrorCode: Int? = null,
    val lengthErrorCode: Int? = null,
    val fullLengthErrorCode: Int? = null,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val metricMeasureCode: Int = MetricMeasure.Meters.code
)
