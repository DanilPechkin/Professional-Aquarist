package com.danilp.professionalaquarist.android.screens.top_bar_menu.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danilp.professionalaquarist.android.R
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    var state by mutableStateOf(SettingsState())

    private val savingEventChannel = Channel<SavingEvent>()
    val savingEvents = savingEventChannel.receiveAsFlow()

    private lateinit var sharedPreferences: SharedPreferences

    private val alkalinityMeasurePref: String =
        context.getString(R.string.alkalinity_measure_id_key)
    private val capacityMeasurePref: String = context.getString(R.string.capacity_measure_id_key)
    private val metricMeasurePref: String = context.getString(R.string.metric_measure_id_key)
    private val temperatureMeasurePref: String =
        context.getString(R.string.temperature_measure_id_key)

    init {
        viewModelScope.launch {
            sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.in_aquarium_info_shared_preferences_key),
                Context.MODE_PRIVATE
            )

            state = state.copy(
                alkalinityMeasureCode = sharedPreferences.getInt(
                    alkalinityMeasurePref,
                    AlkalinityMeasure.DKH.code
                ),
                capacityMeasureCode = sharedPreferences.getInt(
                    capacityMeasurePref,
                    CapacityMeasure.Liters.code
                ),
                metricMeasureCode = sharedPreferences.getInt(
                    metricMeasurePref,
                    MetricMeasure.Meters.code
                ),
                temperatureMeasureCode = sharedPreferences.getInt(
                    temperatureMeasurePref,
                    TemperatureMeasure.Celsius.code
                ),
                capacityList = listOf(
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
                ),
                temperatureList = listOf(
                    context.getString(R.string.temp_measure_celsius),
                    context.getString(R.string.temp_measure_fahrenheit),
                    context.getString(R.string.temp_measure_kelvin)
                ),
                alkalinityList = listOf(
                    context.getString(R.string.alkalinity_measure_dkh),
                    context.getString(R.string.alkalinity_measure_ppm),
                    context.getString(R.string.alkalinity_measure_meql),
                    context.getString(R.string.alkalinity_measure_mgl)
                ),
                metricList = listOf(
                    context.getString(R.string.metric_measure_meters),
                    context.getString(R.string.metric_measure_centimeters),
                    context.getString(R.string.metric_measure_millimeters),
                    context.getString(R.string.metric_measure_feet),
                    context.getString(R.string.metric_measure_inches)
                )
            )


        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SaveButtonPressed -> {
                viewModelScope.launch {
                    with(sharedPreferences.edit()) {
                        this.putInt(alkalinityMeasurePref, state.alkalinityMeasureCode)
                        this.putInt(capacityMeasurePref, state.capacityMeasureCode)
                        this.putInt(metricMeasurePref, state.metricMeasureCode)
                        this.putInt(temperatureMeasurePref, state.temperatureMeasureCode)
                        this.apply()
                    }
                    savingEventChannel.send(SavingEvent.Success)
                }
            }

            is SettingsEvent.DefaultButtonPressed -> {
                state = state.copy(
                    alkalinityMeasureCode = AlkalinityMeasure.DKH.code,
                    capacityMeasureCode = CapacityMeasure.Liters.code,
                    metricMeasureCode = MetricMeasure.Meters.code,
                    temperatureMeasureCode = TemperatureMeasure.Celsius.code
                )
            }

            is SettingsEvent.AlkalinityChanged -> {
                state = state.copy(alkalinityMeasureCode = event.alkalinityCode)
            }

            is SettingsEvent.CapacityChanged -> {
                state = state.copy(capacityMeasureCode = event.capacityCode)
            }

            is SettingsEvent.MetricChanged -> {
                state = state.copy(metricMeasureCode = event.metricCode)
            }

            is SettingsEvent.TemperatureChanged -> {
                state = state.copy(temperatureMeasureCode = event.temperatureCode)
            }
        }
    }

    sealed class SavingEvent {
        object Success : SavingEvent()
    }

}