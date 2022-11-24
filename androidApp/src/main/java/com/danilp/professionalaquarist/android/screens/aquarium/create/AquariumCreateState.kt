package com.danilp.professionalaquarist.android.screens.aquarium.create

import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure

data class AquariumCreateState(
    val aquarium: Aquarium = Aquarium.createEmpty(),
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val isLoading: Boolean = false,
    // Stats
    val name: String = "",
    val liters: String = "",
    val description: String = "",
    val currentIllumination: String = "",

    // Stats errors
    val nameErrorCode: Int? = null,
    val litersErrorCode: Int? = null,
    val currentIlluminationErrorCode: Int? = null
)
