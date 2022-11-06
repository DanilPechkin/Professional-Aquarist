package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.triangle

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.use_case.calculation.aquairum.capacity.CalculateCapacity
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.ConvertMeters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class TriangleCalculatorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val calculateCapacity: CalculateCapacity,
    private val convertLiters: ConvertLiters,
    private val convertMeters: ConvertMeters,
    private val validate: Validate
) : ViewModel() {

    var state by mutableStateOf(TriangleCalculatorState())

    init {
        val sharedPreferences = context.getSharedPreferences(
            context.getString(R.string.in_aquarium_info_shared_preferences_key),
            Context.MODE_PRIVATE
        )

        state = state.copy(
            capacityMeasureCode = sharedPreferences.getInt(
                context.getString(R.string.capacity_measure_id_key),
                CapacityMeasure.Liters.code
            ),
            metricMeasureCode = sharedPreferences.getInt(
                context.getString(R.string.metric_measure_id_key),
                MetricMeasure.Meters.code
            )
        )
    }

    fun onEvent(event: TriangleCalculatorEvent) {
        when (event) {
            TriangleCalculatorEvent.CalculateButtonPressed -> {
                calculateOutputCapacity()
            }

            is TriangleCalculatorEvent.HeightChanged -> {
                state = state.copy(height = event.height)
            }

            is TriangleCalculatorEvent.Side1Changed -> {
                state = state.copy(side1 = event.side1)
            }

            is TriangleCalculatorEvent.Side2Changed -> {
                state = state.copy(side2 = event.side2)
            }

            is TriangleCalculatorEvent.Side3Changed -> {
                state = state.copy(side3 = event.side3)
            }
        }
    }

    private fun calculateOutputCapacity() {

        val heightResult = validate.decimal(state.height, isRequired = true)
        val side1Result = validate.decimal(state.side1, isRequired = true)
        val side2Result = validate.decimal(state.side2, isRequired = true)
        val side3Result = validate.decimal(state.side3, isRequired = true)

        val hasError = listOf(
            heightResult,
            side1Result,
            side2Result,
            side3Result
        ).any { it.errorCode != null }

        if (hasError) {
            state = state.copy(
                heightErrorCode = heightResult.errorCode,
                side1ErrorCode = side1Result.errorCode,
                side2ErrorCode = side2Result.errorCode,
                side3ErrorCode = side3Result.errorCode
            )
            return
        }

        state = state.copy(
            outputCapacity = convertLiters.to(
                capacityMeasureCode = state.capacityMeasureCode,
                liters = convertLiters.from(
                    capacityMeasureCode = CapacityMeasure.Milliliters.code,
                    value = calculateCapacity.triangle(
                        height = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.height.toDouble()
                            ).result
                        ).result,
                        side1 = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.side1.toDouble()
                            ).result
                        ).result,
                        side2 = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.side2.toDouble()
                            ).result
                        ).result,
                        side3 = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.side3.toDouble()
                            ).result
                        ).result
                    ).result
                ).result
            ).result.toString()
        )
    }
}
