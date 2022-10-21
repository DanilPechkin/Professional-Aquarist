package com.danilp.professionalaquarist.android.screens.top_bar_menu.settings

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric.MetricMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class SettingsState(
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val temperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val alkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    val metricMeasureCode: Int = MetricMeasure.Meters.code,
    val capacityList: List<String> = listOf(),
    val temperatureList: List<String> = listOf(),
    val alkalinityList: List<String> = listOf(),
    val metricList: List<String> = listOf()
)
