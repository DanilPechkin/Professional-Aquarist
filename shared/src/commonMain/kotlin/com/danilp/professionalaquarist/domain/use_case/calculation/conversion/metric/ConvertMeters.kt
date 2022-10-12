package com.danilp.professionalaquarist.domain.use_case.calculation.conversion.metric

import com.danilp.professionalaquarist.domain.use_case.calculation.CalculationResult

class ConvertMeters {
    fun to(metricMeasureCode: Int, meters: Double): CalculationResult =
        when (metricMeasureCode) {
            MetricMeasure.Meters.code -> CalculationResult(meters)
            MetricMeasure.Feet.code -> toFeet(meters = meters)
            MetricMeasure.Inches.code -> toInches(meters = meters)
            MetricMeasure.Centimeters.code -> toCentimeters(meters = meters)
            MetricMeasure.Millimeters.code -> toMillimeters(meters = meters)
            else -> CalculationResult(meters)
        }
    fun from(metricMeasureCode: Int, value: Double): CalculationResult =
        when (metricMeasureCode) {
            MetricMeasure.Meters.code -> CalculationResult(value)
            MetricMeasure.Feet.code -> toFeet(feet = value)
            MetricMeasure.Inches.code -> toInches(inches = value)
            MetricMeasure.Centimeters.code -> toCentimeters(centimeters = value)
            MetricMeasure.Millimeters.code -> toMillimeters(millimeters = value)
            else -> CalculationResult(value)
        }
    /**
     * @param meters to calculate centimeters
     * @param centimeters to calculate meters
     */
    private fun toCentimeters(meters: Double = 0.0, centimeters: Double = 0.0): CalculationResult =
        if (centimeters == 0.0)
            CalculationResult(result = meters * 100)
        else
            CalculationResult(result = centimeters / 100)

    /**
     * @param meters to calculate millimeters
     * @param millimeters to calculate meters
     */
    private fun toMillimeters(meters: Double = 0.0, millimeters: Double = 0.0): CalculationResult =
        if (millimeters == 0.0)
            CalculationResult(result = meters * 1000)
        else
            CalculationResult(result = millimeters / 1000)

    /**
     * @param meters to calculate feet
     * @param feet to calculate meters
     */
    private fun toFeet(meters: Double = 0.0, feet: Double = 0.0): CalculationResult =
        if (feet == 0.0)
            CalculationResult(result = meters * 3.28084)
        else
            CalculationResult(result = feet / 3.28084)

    /**
     * @param meters to calculate inches
     * @param inches to calculate meters
     */
    private fun toInches(meters: Double = 0.0, inches: Double = 0.0): CalculationResult =
        if (inches == 0.0)
            CalculationResult(result = meters * 39.37008)
        else
            CalculationResult(result = inches / 39.37008)
}