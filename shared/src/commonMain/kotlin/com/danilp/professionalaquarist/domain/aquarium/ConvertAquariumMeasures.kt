package com.danilp.professionalaquarist.domain.aquarium

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.ConvertLiters
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.ConvertCelsius

class ConvertAquariumMeasures {

    private val convertCelsius: ConvertCelsius = ConvertCelsius()
    private val convertLiters: ConvertLiters = ConvertLiters()
    private val convertDKH: ConvertDKH = ConvertDKH()

    fun to(
        aquarium: Aquarium,
        alkalinityMeasureCode: Int,
        capacityMeasureCode: Int,
        temperatureMeasureCode: Int
    ): Aquarium =
        aquarium.copy(
            minTemperature = aquarium.minTemperature?.let {
                convertCelsius.to(
                    temperatureMeasureCode,
                    it
                ).result
            },
            maxTemperature = aquarium.maxTemperature?.let {
                convertCelsius.to(
                    temperatureMeasureCode,
                    it
                ).result
            },
            minPh = aquarium.minPh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxPh = aquarium.maxPh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minGh = aquarium.minGh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxGh = aquarium.maxGh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minKh = aquarium.minKh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxKh = aquarium.maxKh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            liters = aquarium.liters?.let {
                convertLiters.to(
                    capacityMeasureCode,
                    it
                ).result
            }
        )

    fun from(
        aquarium: Aquarium,
        alkalinityMeasureCode: Int,
        capacityMeasureCode: Int,
        temperatureMeasureCode: Int
    ): Aquarium =
        aquarium.copy(
            minTemperature = aquarium.minTemperature?.let {
                convertCelsius.from(
                    temperatureMeasureCode,
                    it
                ).result
            },
            maxTemperature = aquarium.maxTemperature?.let {
                convertCelsius.from(
                    temperatureMeasureCode,
                    it
                ).result
            },
            minPh = aquarium.minPh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxPh = aquarium.maxPh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minGh = aquarium.minGh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxGh = aquarium.maxGh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minKh = aquarium.minKh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxKh = aquarium.maxKh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            liters = aquarium.liters?.let {
                convertLiters.from(
                    capacityMeasureCode,
                    it
                ).result
            }
        )

}