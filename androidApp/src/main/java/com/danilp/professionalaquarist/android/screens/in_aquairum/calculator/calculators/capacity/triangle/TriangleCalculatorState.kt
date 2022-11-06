package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.triangle

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure

data class TriangleCalculatorState(
    val height: String = "",
    val side1: String = "",
    val side2: String = "",
    val side3: String = "",
    val outputCapacity: String = "",
    val heightErrorCode: Int? = null,
    val side1ErrorCode: Int? = null,
    val side2ErrorCode: Int? = null,
    val side3ErrorCode: Int? = null,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val metricMeasureCode: Int = MetricMeasure.Meters.code
)
