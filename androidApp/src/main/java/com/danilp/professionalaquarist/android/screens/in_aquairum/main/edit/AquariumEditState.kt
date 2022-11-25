package com.danilp.professionalaquarist.android.screens.in_aquairum.main.edit

import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class AquariumEditState(
    val aquarium: Aquarium = Aquarium.createEmpty(),
    val isLoading: Boolean = false,
    val temperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val alkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    // Stats
    val name: String = "",
    val liters: String = "",
    val description: String = "",
    val minIllumination: String = "",
    val currentIllumination: String = "",
    val minCO2: String = "",
    val currentCO2: String = "",
    val minTemperature: String = "",
    val maxTemperature: String = "",
    val currentTemperature: String = "",
    val minPh: String = "",
    val maxPh: String = "",
    val currentPh: String = "",
    val minGh: String = "",
    val maxGh: String = "",
    val currentGh: String = "",
    val minKh: String = "",
    val maxKh: String = "",
    val currentKh: String = "",
    val minK: String = "",
    val maxK: String = "",
    val currentK: String = "",
    val minNO3: String = "",
    val maxNO3: String = "",
    val currentNO3: String = "",
    val minFe: String = "",
    val maxFe: String = "",
    val currentFe: String = "",
    val minCa: String = "",
    val maxCa: String = "",
    val currentCa: String = "",
    val minMg: String = "",
    val maxMg: String = "",
    val currentMg: String = "",
    val minPO4: String = "",
    val maxPO4: String = "",
    val currentPO4: String = "",
    val minAmmonia: String = "",
    val maxAmmonia: String = "",
    val currentAmmonia: String = "",
    val currentTags: List<String> = emptyList(),
    val requiredTags: List<String> = emptyList(),

    // Stats errors
    val nameErrorCode: Int? = null,
    val litersErrorCode: Int? = null,
)
