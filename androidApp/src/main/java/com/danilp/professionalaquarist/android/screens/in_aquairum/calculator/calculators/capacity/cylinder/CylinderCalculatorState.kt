package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.cylinder

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure

data class CylinderCalculatorState(
    val height: String = "",
    val diameter: String = "",
    val outputCapacity: String = "",
    val heightErrorCode: Int? = null,
    val diameterErrorCode: Int? = null,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val metricMeasureCode: Int = MetricMeasure.Meters.code
)
