package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity.regular_polygon

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.screens.top_bar_menu.settings.SharedPrefs
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.use_case.calculation.aquairum.capacity.CalculateCapacity
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.ConvertMeters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure
import com.danilp.professionalaquarist.domain.use_case.validation.Validate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegularPolygonCalculatorViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val calculateCapacity: CalculateCapacity,
    private val convertLiters: ConvertLiters,
    private val convertMeters: ConvertMeters,
    private val validate: Validate,
    private val aquariumDataSource: AquariumDataSource
) : ViewModel() {

    var state by mutableStateOf(RegularPolygonCalculatorState())

    var aquariumId: Long? = null

    init {
        val sharedPreferences = context.getSharedPreferences(
            SharedPrefs.InAquariumInfo.key,
            Context.MODE_PRIVATE
        )

        aquariumId = sharedPreferences.getLong(SharedPrefs.CurrentAquarium.key, -1)

        state = state.copy(
            capacityMeasureCode = sharedPreferences.getInt(
                SharedPrefs.CapacityMeasure.key,
                CapacityMeasure.Liters.code
            ),
            metricMeasureCode = sharedPreferences.getInt(
                SharedPrefs.MetricMeasure.key,
                MetricMeasure.Meters.code
            )
        )
    }

    fun onEvent(event: RegularPolygonCalculatorEvent) {
        when (event) {
            RegularPolygonCalculatorEvent.CalculateButtonPressed -> {
                calculateOutputCapacity()
            }

            is RegularPolygonCalculatorEvent.HeightChanged -> {
                state = state.copy(height = event.height, heightErrorCode = null)
            }

            is RegularPolygonCalculatorEvent.SidesChanged -> {
                state = state.copy(sides = event.sides, sidesErrorCode = null)
            }

            is RegularPolygonCalculatorEvent.LengthChanged -> {
                state = state.copy(length = event.length, lengthErrorCode = null)
            }

            RegularPolygonCalculatorEvent.ApplyButtonPressed -> {
                applyForAquarium()
            }
        }
    }

    private fun calculateOutputCapacity() {

        val heightResult = validate.decimal(state.height, isRequired = true)
        val sidesResult = validate.integer(state.sides, isRequired = true)
        val lengthResult = validate.integer(state.length, isRequired = true)

        val hasError = listOf(
            heightResult,
            sidesResult,
            lengthResult
        ).any { it.errorCode != null }

        if (hasError) {
            state = state.copy(
                heightErrorCode = heightResult.errorCode,
                sidesErrorCode = sidesResult.errorCode,
                lengthErrorCode = lengthResult.errorCode
            )
            return
        }

        state = state.copy(
            outputCapacity = convertLiters.to(
                capacityMeasureCode = state.capacityMeasureCode,
                liters = convertLiters.from(
                    capacityMeasureCode = CapacityMeasure.Milliliters.code,
                    value = calculateCapacity.regularPolygon(
                        height = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.height.toDouble()
                            ).result
                        ).result,
                        sides = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.sides.toDouble()
                            ).result
                        ).result.toInt(),
                        length = convertMeters.to(
                            metricMeasureCode = MetricMeasure.Centimeters.code,
                            meters = convertMeters.from(
                                state.metricMeasureCode,
                                state.length.toDouble()
                            ).result
                        ).result
                    ).result
                ).result
            ).result.toString()
        )
    }

    private fun applyForAquarium() {
        viewModelScope.launch(Dispatchers.IO) {
            aquariumDataSource.getAquariumById(aquariumId ?: -1)?.let { aquarium ->
                aquariumDataSource.insertAquarium(
                    aquarium.copy(
                        liters = convertLiters.from(
                            state.capacityMeasureCode,
                            state.outputCapacity.toDouble()
                        ).result
                    )
                )
            }
            aquariumDataSource.refreshAquariumById(aquariumId ?: -1)
        }
    }
}
