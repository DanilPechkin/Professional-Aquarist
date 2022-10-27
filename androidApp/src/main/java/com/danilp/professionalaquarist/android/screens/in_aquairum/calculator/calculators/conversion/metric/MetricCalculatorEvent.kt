package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.metric

sealed interface MetricCalculatorEvent {

    object ConvertButtonPressed : MetricCalculatorEvent

    data class InputMetricChanged(val metric: String) : MetricCalculatorEvent

    data class InputMetricMeasureCodeChanged(val code: Int) : MetricCalculatorEvent

    data class OutputMetricMeasureCodeChanged(val code: Int) : MetricCalculatorEvent
}
