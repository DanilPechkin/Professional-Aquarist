package com.danilp.professionalaquarist.android.screens.aquarium.edit

import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class AquariumEditState(
    val aquarium: Aquarium = Aquarium.createEmpty(),
    val temperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val alkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    // Stats
    val name: String = "",
    val liters: String = "",
    val description: String = "",
    val minIllumination: String = "",
    val minCO2: String = "",
    val minTemperature: String = "",
    val maxTemperature: String = "",
    val minPh: String = "",
    val maxPh: String = "",
    val minGh: String = "",
    val maxGh: String = "",
    val minKh: String = "",
    val maxKh: String = "",
    val minK: String = "",
    val maxK: String = "",
    val minNO3: String = "",
    val maxNO3: String = "",
    val minFe: String = "",
    val maxFe: String = "",
    val minCa: String = "",
    val maxCa: String = "",
    val minMg: String = "",
    val maxMg: String = "",
    val minPO4: String = "",
    val maxPO4: String = "",
    val minAmmonia: String = "",
    val maxAmmonia: String = "",

    // Stats errors
    val nameErrorCode: Int? = null,
    val litersErrorCode: Int? = null,
    val minIlluminationErrorCode: Int? = null,
    val minCO2ErrorCode: Int? = null,
    val minTemperatureErrorCode: Int? = null,
    val maxTemperatureErrorCode: Int? = null,
    val minPhErrorCode: Int? = null,
    val maxPhErrorCode: Int? = null,
    val minGhErrorCode: Int? = null,
    val maxGhErrorCode: Int? = null,
    val minKhErrorCode: Int? = null,
    val maxKhErrorCode: Int? = null,
    val minKErrorCode: Int? = null,
    val maxKErrorCode: Int? = null,
    val minNO3ErrorCode: Int? = null,
    val maxNO3ErrorCode: Int? = null,
    val minFeErrorCode: Int? = null,
    val maxFeErrorCode: Int? = null,
    val minCaErrorCode: Int? = null,
    val maxCaErrorCode: Int? = null,
    val minMgErrorCode: Int? = null,
    val maxMgErrorCode: Int? = null,
    val minPO4ErrorCode: Int? = null,
    val maxPO4ErrorCode: Int? = null,
    val minAmmoniaErrorCode: Int? = null,
    val maxAmmoniaErrorCode: Int? = null
)
