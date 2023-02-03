package com.danilp.professionalaquarist.android.screens.top_bar_menu.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    var state by mutableStateOf(SettingsState())

    private val savingEventChannel = Channel<SavingEvent>()
    val savingEvents = savingEventChannel.receiveAsFlow()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var resources: Resources
    private lateinit var configuration: Configuration

    init {
        viewModelScope.launch {

            resources = context.resources
            configuration = resources.configuration

            sharedPreferences = context.getSharedPreferences(
                SharedPrefs.InAquariumInfo.key,
                Context.MODE_PRIVATE
            )

            state = state.copy(
                alkalinityMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.AlkalinityMeasure.key,
                    AlkalinityMeasure.DKH.code
                ),
                capacityMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.CapacityMeasure.key,
                    CapacityMeasure.Liters.code
                ),
                metricMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.MetricMeasure.key,
                    MetricMeasure.Meters.code
                ),
                temperatureMeasureCode = sharedPreferences.getInt(
                    SharedPrefs.TemperatureMeasure.key,
                    TemperatureMeasure.Celsius.code
                ),
                language = Language.values().find {
                    it.code ==
                            (sharedPreferences.getString(
                                SharedPrefs.Language.key,
                                Locale.getDefault().language
                            ) ?: Locale.getDefault().language)
                } ?: Language.English,
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
                    context.getString(R.string.temperature_measure_celsius) +
                            " (${context.getString(R.string.temperature_measure_celsius_short)})",
                    context.getString(R.string.temperature_measure_fahrenheit) +
                            " (${context.getString(R.string.temperature_measure_fahrenheit_short)})",
                    context.getString(R.string.temperature_measure_kelvin) +
                            " (${context.getString(R.string.temperature_measure_kelvin_short)})"
                ),
                alkalinityList = listOf(
                    context.getString(R.string.alkalinity_measure_dh) +
                            " (${context.getString(R.string.alkalinity_measure_dh_short)})",
                    context.getString(R.string.alkalinity_measure_ppm) +
                            " (${context.getString(R.string.alkalinity_measure_ppm_short)})",
                    context.getString(R.string.alkalinity_measure_meql) +
                            " (${context.getString(R.string.alkalinity_measure_meql_short)})",
                    context.getString(R.string.alkalinity_measure_mgl) +
                            " (${context.getString(R.string.alkalinity_measure_mgl_short)})"
                ),
                metricList = listOf(
                    context.getString(R.string.metric_measure_meters) +
                            " (${context.getString(R.string.metric_measure_meters_short)})",
                    context.getString(R.string.metric_measure_centimeters) +
                            " (${context.getString(R.string.metric_measure_centimeters_short)})",
                    context.getString(R.string.metric_measure_millimeters) +
                            " (${context.getString(R.string.metric_measure_millimeters_short)})",
                    context.getString(R.string.metric_measure_feet) +
                            " (${context.getString(R.string.metric_measure_feet_short)})",
                    context.getString(R.string.metric_measure_inches) +
                            " (${context.getString(R.string.metric_measure_inches_short)})"
                )
            )
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SaveButtonPressed -> {
                viewModelScope.launch {
                    saveSettings()
                    savingEventChannel.send(SavingEvent.Saved)
                    savingEventChannel.send(SavingEvent.Done)
                }
            }

            is SettingsEvent.DefaultButtonPressed -> {
                viewModelScope.launch {
                    state = state.copy(
                        alkalinityMeasureCode = AlkalinityMeasure.DKH.code,
                        capacityMeasureCode = CapacityMeasure.Liters.code,
                        metricMeasureCode = MetricMeasure.Meters.code,
                        temperatureMeasureCode = TemperatureMeasure.Celsius.code,
                        language = Language.values().find {
                            it.code == Locale.getDefault().language
                        } ?: Language.English
                    )
                    saveSettings()
                    savingEventChannel.send(SavingEvent.Saved)
                }
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

            is SettingsEvent.LanguageSelected -> {
                state = state.copy(language = event.language)
            }
        }
    }

    private fun saveSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            with(sharedPreferences.edit()) {
                this.putInt(SharedPrefs.AlkalinityMeasure.key, state.alkalinityMeasureCode)
                this.putInt(SharedPrefs.CapacityMeasure.key, state.capacityMeasureCode)
                this.putInt(SharedPrefs.MetricMeasure.key, state.metricMeasureCode)
                this.putInt(
                    SharedPrefs.TemperatureMeasure.key,
                    state.temperatureMeasureCode
                )
                this.putString(SharedPrefs.Language.key, state.language.code)
                this.apply()
            }
            Locale.setDefault(Locale(state.language.code))
            configuration.setLocale(Locale(state.language.code))
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }

    sealed class SavingEvent {
        object Done : SavingEvent()
        object Saved : SavingEvent()
    }
}
