package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.cylinder

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
class CylinderCalculatorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val calculateCapacity: CalculateCapacity,
    private val convertLiters: ConvertLiters,
    private val convertMeters: ConvertMeters,
    private val validate: Validate
) : ViewModel() {

    var state by mutableStateOf(CylinderCalculatorState())

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

    fun onEvent(event: CylinderCalculatorEvent) {
        when (event) {
            CylinderCalculatorEvent.CalculateButtonPressed -> {
                calculateOutputCapacity()
            }

            is CylinderCalculatorEvent.HeightChanged -> {
                state = state.copy(height = event.height)
            }

            is CylinderCalculatorEvent.DiameterChanged -> {
                state = state.copy(diameter = event.diameter)
            }
        }
    }

    private fun calculateOutputCapacity() {

        val heightResult = validate.decimal(state.height, isRequired = true)
        val diameterResult = validate.decimal(state.diameter, isRequired = true)

        val hasError = listOf(
            heightResult,
            diameterResult
        ).any { it.errorCode != null }

        if (hasError) {
            state = state.copy(
                heightErrorCode = heightResult.errorCode,
                diameterErrorCode = diameterResult.errorCode
            )
            return
        }

        state = state.copy(
            outputCapacity = convertLiters.to(
                capacityMeasureCode = state.capacityMeasureCode,
                liters = convertLiters.from(
                    capacityMeasureCode = CapacityMeasure.Milliliters.code,
                    value = calculateCapacity.cylinder(
                        height = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.height.toDouble()
                            ).result
                        ).result,
                        diameter = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.diameter.toDouble()
                            ).result
                        ).result
                    ).result
                ).result
            ).result.toString()
        )
    }
}
