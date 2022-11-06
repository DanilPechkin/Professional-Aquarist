package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.elliptical_cylinder

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure

data class EllipticalCylinderCalculatorState(
    val height: String = "",
    val width: String = "",
    val length: String = "",
    val outputCapacity: String = "",
    val heightErrorCode: Int? = null,
    val widthErrorCode: Int? = null,
    val lengthErrorCode: Int? = null,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val metricMeasureCode: Int = MetricMeasure.Meters.code
)
