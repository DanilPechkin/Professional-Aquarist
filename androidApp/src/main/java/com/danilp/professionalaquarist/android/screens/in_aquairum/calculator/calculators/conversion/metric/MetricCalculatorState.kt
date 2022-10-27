package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.metric

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure

data class MetricCalculatorState(
    val inputMetric: String = "",
    val inputMetricMeasureCode: Int = MetricMeasure.Meters.code,
    val inputMetricErrorCode: Int? = null,
    val outputMetric: String = "",
    val outputMetricMeasureCode: Int = MetricMeasure.Meters.code,
    val metricMeasuresList: List<String> = listOf()
)
