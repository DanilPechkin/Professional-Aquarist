package com.danilp.professionalaquarist.domain.use_case.calculation.fresh_water

import com.danilp.professionalaquarist.domain.use_case.calculation.CalculationResult
import kotlin.math.pow

class CalculateFreshCO2 {
    fun execute(kH: Double, pH: Double): CalculationResult =
        CalculationResult(result = .0 * kH * (10.0).pow(7.0 - pH))
}