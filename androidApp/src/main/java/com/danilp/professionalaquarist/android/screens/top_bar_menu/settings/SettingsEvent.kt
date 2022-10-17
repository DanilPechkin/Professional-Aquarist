package com.danilp.professionalaquarist.android.screens.top_bar_menu.settings

sealed class SettingsEvent {
    object SaveButtonPressed : SettingsEvent()
    object DefaultButtonPressed : SettingsEvent()
    data class CapacityChanged(val capacityCode: Int) : SettingsEvent()
    data class MetricChanged(val metricCode: Int) : SettingsEvent()
    data class AlkalinityChanged(val alkalinityCode: Int) : SettingsEvent()
    data class TemperatureChanged(val temperatureCode: Int) : SettingsEvent()
}