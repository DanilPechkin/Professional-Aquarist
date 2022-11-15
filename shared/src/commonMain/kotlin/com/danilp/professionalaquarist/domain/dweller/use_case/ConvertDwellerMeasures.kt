package com.danilp.professionalaquarist.domain.dweller.use_case

import com.danilp.professionalaquarist.domain.dweller.Dweller
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.ConvertCelsius

class ConvertDwellerMeasures {

    private val convertCelsius: ConvertCelsius = ConvertCelsius()
    private val convertLiters: ConvertLiters = ConvertLiters()
    private val convertDKH: ConvertDKH = ConvertDKH()

    fun to(
        dweller: Dweller,
        alkalinityMeasureCode: Int,
        capacityMeasureCode: Int,
        temperatureMeasureCode: Int
    ): Dweller =
        dweller.copy(
            minTemperature = dweller.minTemperature?.let {
                convertCelsius.to(
                    temperatureMeasureCode,
                    it
                ).result
            },
            maxTemperature = dweller.maxTemperature?.let {
                convertCelsius.to(
                    temperatureMeasureCode,
                    it
                ).result
            },
            minPh = dweller.minPh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxPh = dweller.maxPh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minGh = dweller.minGh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxGh = dweller.maxGh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minKh = dweller.minKh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxKh = dweller.maxKh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            liters = dweller.liters?.let {
                convertLiters.to(
                    capacityMeasureCode,
                    it
                ).result
            }
        )

    fun from(
        dweller: Dweller,
        alkalinityMeasureCode: Int,
        capacityMeasureCode: Int,
        temperatureMeasureCode: Int
    ): Dweller =
        dweller.copy(
            minTemperature = dweller.minTemperature?.let {
                convertCelsius.from(
                    temperatureMeasureCode,
                    it
                ).result
            },
            maxTemperature = dweller.maxTemperature?.let {
                convertCelsius.from(
                    temperatureMeasureCode,
                    it
                ).result
            },
            minPh = dweller.minPh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxPh = dweller.maxPh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minGh = dweller.minGh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxGh = dweller.maxGh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minKh = dweller.minKh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxKh = dweller.maxKh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            liters = dweller.liters?.let {
                convertLiters.from(
                    capacityMeasureCode,
                    it
                ).result
            }
        )

}