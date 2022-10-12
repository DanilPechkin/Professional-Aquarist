package com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature

import com.danilp.professionalaquarist.domain.use_case.calculation.CalculationResult

/**
 * Celsius also can be used as Centigrade
 */
class ConvertCelsius {
    fun to(temperatureMeasureCode: Int, celsius: Double): CalculationResult =
        when (temperatureMeasureCode) {
            TemperatureMeasure.Celsius.code -> CalculationResult(celsius)
            TemperatureMeasure.Fahrenheit.code -> toFahrenheit(celsius = celsius)
            TemperatureMeasure.Kelvin.code -> toKelvin(celsius = celsius)
            else -> CalculationResult(celsius)
        }
    fun from(temperatureMeasureCode: Int, value: Double): CalculationResult =
        when (temperatureMeasureCode) {
            TemperatureMeasure.Celsius.code -> CalculationResult(value)
            TemperatureMeasure.Fahrenheit.code -> toFahrenheit(fahrenheit = value)
            TemperatureMeasure.Kelvin.code -> toKelvin(kelvin = value)
            else -> CalculationResult(value)
        }
    /**
     * @param celsius to calculate fahrenheit
     * @param fahrenheit to calculate celsius
     */
    private fun toFahrenheit(celsius: Double = 0.0, fahrenheit: Double = 0.0): CalculationResult =
        if (fahrenheit == 0.0)
            CalculationResult(result = (celsius * 1.8) + 32)
        else
            CalculationResult(result = (fahrenheit - 32) / 1.8)

    /**
     * @param celsius to calculate kelvin
     * @param kelvin to calculate celsius
     */
    private fun toKelvin(celsius: Double = 0.0, kelvin: Double = 0.0): CalculationResult =
        if (kelvin == 0.0)
            CalculationResult(result = celsius + 273.15)
        else
            CalculationResult(result = kelvin - 273.15)
}