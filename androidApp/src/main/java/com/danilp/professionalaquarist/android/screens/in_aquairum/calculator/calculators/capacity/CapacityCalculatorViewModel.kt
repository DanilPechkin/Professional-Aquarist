package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class CapacityCalculatorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val convertLiters: ConvertLiters,
    private val validate: Validate
) : ViewModel() {
    var state by mutableStateOf(CapacityCalculatorState())

    init {
        state = state.copy(
            capacityMeasuresList = listOf(
                context.getString(R.string.capacity_measure_liters),
                context.getString(R.string.capacity_measure_cubic_feet),
                context.getString(R.string.capacity_measure_us_cups),
                context.getString(R.string.capacity_measure_teaspoons),
                context.getString(R.string.capacity_measure_tablespoons),
                context.getString(R.string.capacity_measure_milliliters),
                context.getString(R.string.capacity_measure_metric_cups),
                context.getString(R.string.capacity_measure_gallons),
                context.getString(R.string.capacity_measure_cubic_meters),
                context.getString(R.string.capacity_measure_cubic_inches),
                context.getString(R.string.capacity_measure_cubic_centimeters)
            )
        )
    }

    fun onEvent(event: CapacityCalculatorEvent) {
        when (event) {
            CapacityCalculatorEvent.ConvertButtonPressed -> {
                calculateOutputCapacity()
            }

            is CapacityCalculatorEvent.InputCapacityChanged -> {
                state = state.copy(inputCapacity = event.capacity)
            }

            is CapacityCalculatorEvent.InputCapacityMeasureCodeChanged -> {
                state = state.copy(inputCapacityMeasureCode = event.code)
            }

            is CapacityCalculatorEvent.OutputCapacityMeasureCodeChanged -> {
                state = state.copy(outputCapacityMeasureCode = event.code)
            }
        }
    }

    private fun calculateOutputCapacity() {
        val inputCapacityResult = validate.decimal(
            value = state.inputCapacity,
            isRequired = true
        )
        if (inputCapacityResult.errorCode != null) {
            state = state.copy(inputCapacityErrorCode = inputCapacityResult.errorCode)
            return
        }

        val litersCapacity = convertLiters.from(
            state.inputCapacityMeasureCode,
            state.inputCapacity.toDouble()
        ).result
        val calculatedOutputCapacity = convertLiters.to(
            state.outputCapacityMeasureCode,
            litersCapacity
        ).result

        state = state.copy(
            outputCapacity = calculatedOutputCapacity.toString()
        )
    }
}
