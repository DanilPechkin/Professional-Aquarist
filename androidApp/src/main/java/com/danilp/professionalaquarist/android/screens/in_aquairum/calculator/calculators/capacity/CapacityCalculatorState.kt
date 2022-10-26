package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.capacity

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure

data class CapacityCalculatorState(
    val inputCapacity: String = "",
    val inputCapacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val inputCapacityErrorCode: Int? = null,
    val outputCapacity: String = "",
    val outputCapacityMeasureCode: Int = CapacityMeasure.Liters.code,
    val capacityMeasuresList: List<String> = listOf()
)
