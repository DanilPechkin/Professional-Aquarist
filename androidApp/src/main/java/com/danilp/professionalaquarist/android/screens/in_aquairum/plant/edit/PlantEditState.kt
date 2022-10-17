package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.edit

import com.danilp.professionalaquarist.domain.plant.Plant
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class PlantEditState(
    val plant: Plant = Plant.createEmpty(),
    val temperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val alkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    // Stats
    val name: String = "",
    val genus: String = "",
    val minTemperature: String = "",
    val maxTemperature: String = "",
    val minPh: String = "",
    val maxPh: String = "",
    val minGh: String = "",
    val maxGh: String = "",
    val minKh: String = "",
    val maxKh: String = "",
    val minCO2: String = "",
    val minIllumination: String = "",
    val description: String = "",
    // Stats errors
    val nameErrorCode: Int? = null,
    /*TODO: val genusError: Int? = null,*/
    val minTemperatureErrorCode: Int? = null,
    val maxTemperatureErrorCode: Int? = null,
    val minPhErrorCode: Int? = null,
    val maxPhErrorCode: Int? = null,
    val minGhErrorCode: Int? = null,
    val maxGhErrorCode: Int? = null,
    val minKhErrorCode: Int? = null,
    val maxKhErrorCode: Int? = null,
    val minCO2ErrorCode: Int? = null,
    val minIlluminationErrorCode: Int? = null
)
