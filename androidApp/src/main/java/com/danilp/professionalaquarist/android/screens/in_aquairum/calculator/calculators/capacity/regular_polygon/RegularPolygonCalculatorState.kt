package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.regular_polygon

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure

data class RegularPolygonCalculatorState(
    val height: String = "",
    val sides: String = "",
    val length: String = "",
    val outputCapacity: String = "",
    val heightErrorCode: Int? = null,
    val sidesErrorCode: Int? = null,
    val lengthErrorCode: Int? = null,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val metricMeasureCode: Int = MetricMeasure.Meters.code
)
