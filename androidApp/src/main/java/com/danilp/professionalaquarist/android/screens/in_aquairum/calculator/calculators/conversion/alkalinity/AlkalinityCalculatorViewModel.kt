package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.alkalinity

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AlkalinityCalculatorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val convertDKH: ConvertDKH,
    private val validate: Validate
) : ViewModel() {
    var state by mutableStateOf(AlkalinityCalculatorState())

    init {
        state = state.copy(
            alkalinityMeasuresList = listOf(
                context.getString(R.string.alkalinity_measure_dh_short),
                context.getString(R.string.alkalinity_measure_ppm_short),
                context.getString(R.string.alkalinity_measure_meql_short),
                context.getString(R.string.alkalinity_measure_mgl_short)
            )
        )
    }

    fun onEvent(event: AlkalinityCalculatorEvent) {
        when (event) {
            AlkalinityCalculatorEvent.ConvertButtonPressed -> {
                calculateOutputAlkalinity()
            }

            is AlkalinityCalculatorEvent.InputAlkalinityChanged -> {
                state = state.copy(inputAlkalinity = event.alkalinity)
            }

            is AlkalinityCalculatorEvent.InputAlkalinityMeasureCodeChanged -> {
                state = state.copy(inputAlkalinityMeasureCode = event.code)
            }

            is AlkalinityCalculatorEvent.OutputAlkalinityMeasureCodeChanged -> {
                state = state.copy(outputAlkalinityMeasureCode = event.code)
            }
        }
    }

    private fun calculateOutputAlkalinity() {
        val inputAlkalinityResult = validate.decimal(
            value = state.inputAlkalinity,
            isRequired = true
        )
        if (inputAlkalinityResult.errorCode != null) {
            state = state.copy(inputAlkalinityErrorCode = inputAlkalinityResult.errorCode)
            return
        }

        val dkhAlkalinity = convertDKH.from(
            state.inputAlkalinityMeasureCode,
            state.inputAlkalinity.toDouble()
        ).result
        val calculatedOutputAlkalinity = convertDKH.to(
            state.outputAlkalinityMeasureCode,
            dkhAlkalinity
        ).result

        state = state.copy(
            outputAlkalinity = calculatedOutputAlkalinity.toString()
        )
    }
}
