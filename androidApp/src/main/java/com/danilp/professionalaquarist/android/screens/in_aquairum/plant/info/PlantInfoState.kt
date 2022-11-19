package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.info

import com.danilp.professionalaquarist.domain.plant.Plant
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class PlantInfoState(
    val plant: Plant = Plant.createEmpty(),
    val temperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val alkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code
)
