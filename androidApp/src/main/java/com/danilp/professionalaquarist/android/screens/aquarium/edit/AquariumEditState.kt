package com.danilp.professionalaquarist.android.screens.aquarium.edit

import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure

data class AquariumEditState(
    val aquarium: Aquarium = Aquarium.createEmpty(),
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val nameErrorCode: Int? = null,
    val litersErrorCode: Int? = null,
    val name: String = "",
    val liters: String = "",
    val description: String = ""
)
