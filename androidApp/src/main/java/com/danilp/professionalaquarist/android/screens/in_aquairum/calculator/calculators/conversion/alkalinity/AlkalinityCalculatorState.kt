package com.danilp.professionalaquarist.android.screens.in_aquairum.calculator.calculators.conversion.alkalinity

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure

data class AlkalinityCalculatorState(
    val inputAlkalinity: String = "",
    val inputAlkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    val inputAlkalinityErrorCode: Int? = null,
    val outputAlkalinity: String = "",
    val outputAlkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    val alkalinityMeasuresList: List<String> = listOf()
)
