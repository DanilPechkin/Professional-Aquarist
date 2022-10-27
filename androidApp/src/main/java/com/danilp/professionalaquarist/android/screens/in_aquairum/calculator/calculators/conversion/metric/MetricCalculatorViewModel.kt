package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.metric

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.ConvertMeters
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MetricCalculatorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val convertMeters: ConvertMeters,
    private val validate: Validate
) : ViewModel() {
    var state by mutableStateOf(MetricCalculatorState())

    init {
        state = state.copy(
            metricMeasuresList = listOf(
                context.getString(R.string.metric_measure_meters),
                context.getString(R.string.metric_measure_centimeters),
                context.getString(R.string.metric_measure_millimeters),
                context.getString(R.string.metric_measure_feet),
                context.getString(R.string.metric_measure_inches)
            )
        )
    }

    fun onEvent(event: MetricCalculatorEvent) {
        when (event) {
            MetricCalculatorEvent.ConvertButtonPressed -> {
                calculateOutputMetric()
            }

            is MetricCalculatorEvent.InputMetricChanged -> {
                state = state.copy(inputMetric = event.metric)
            }

            is MetricCalculatorEvent.InputMetricMeasureCodeChanged -> {
                state = state.copy(inputMetricMeasureCode = event.code)
            }

            is MetricCalculatorEvent.OutputMetricMeasureCodeChanged -> {
                state = state.copy(outputMetricMeasureCode = event.code)
            }
        }
    }

    private fun calculateOutputMetric() {
        val inputMetricResult = validate.decimal(
            value = state.inputMetric,
            isRequired = true
        )
        if (inputMetricResult.errorCode != null) {
            state = state.copy(inputMetricErrorCode = inputMetricResult.errorCode)
            return
        }

        val metersMetric = convertMeters.from(
            state.inputMetricMeasureCode,
            state.inputMetric.toDouble()
        ).result
        val calculatedOutputMetric = convertMeters.to(
            state.outputMetricMeasureCode,
            metersMetric
        ).result

        state = state.copy(
            outputMetric = calculatedOutputMetric.toString()
        )
    }
}
