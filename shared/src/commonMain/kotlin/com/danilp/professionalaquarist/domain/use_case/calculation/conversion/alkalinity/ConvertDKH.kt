package com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity

import com.danilp.professionalaquarist.domain.use_case.calculation.CalculationResult

class ConvertDKH {
    fun to(alkalinityMeasureCode: Int, dKH: Double): CalculationResult =
        when (alkalinityMeasureCode) {
            AlkalinityMeasure.DKH.code -> CalculationResult(dKH)
            AlkalinityMeasure.PPM.code -> toPpm(dKH = dKH)
            AlkalinityMeasure.MEQL.code -> toMeqL(dKH = dKH)
            AlkalinityMeasure.MGL.code -> toPpm(dKH = dKH)
            else -> CalculationResult(dKH)
        }

    fun from(alkalinityMeasureCode: Int, value: Double): CalculationResult =
        when (alkalinityMeasureCode) {
            AlkalinityMeasure.DKH.code -> CalculationResult(value)
            AlkalinityMeasure.PPM.code -> toPpm(ppm = value)
            AlkalinityMeasure.MEQL.code -> toMeqL(meqL = value)
            AlkalinityMeasure.MGL.code -> toPpm(ppm = value)
            else -> CalculationResult(value)
        }
    /**
     * @param dKH to calculate meqL
     * @param meqL to calculate dKH
     */
    private fun toMeqL(dKH: Double = 0.0, meqL: Double = 0.0): CalculationResult =
        if (meqL == 0.0)
            CalculationResult(result = dKH * 0.357)
        else
            CalculationResult(result = meqL / 0.357)

    /**
     * Ppm also can be used as mg/L
     * @param dKH to calculate ppm
     * @param ppm to calculate dKH
     */
    private fun toPpm(dKH: Double = 0.0, ppm: Double = 0.0): CalculationResult =
        if (ppm == 0.0)
            CalculationResult(result = dKH * 17.887)
        else
            CalculationResult(result = ppm / 17.887)
}