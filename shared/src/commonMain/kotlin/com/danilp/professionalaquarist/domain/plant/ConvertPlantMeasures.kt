package com.danilp.professionalaquarist.domain.plant

import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.ConvertDKH
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.ConvertCelsius

class ConvertPlantMeasures {

    private val convertCelsius: ConvertCelsius = ConvertCelsius()
    private val convertDKH: ConvertDKH = ConvertDKH()

    fun to(
        plant: Plant,
        alkalinityMeasureCode: Int,
        temperatureMeasureCode: Int
    ): Plant =
        plant.copy(
            minTemperature = plant.minTemperature?.let {
                convertCelsius.to(
                    temperatureMeasureCode,
                    it
                ).result
            },
            maxTemperature = plant.maxTemperature?.let {
                convertCelsius.to(
                    temperatureMeasureCode,
                    it
                ).result
            },
            minPh = plant.minPh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxPh = plant.maxPh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minGh = plant.minGh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxGh = plant.maxGh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minKh = plant.minKh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxKh = plant.maxKh?.let {
                convertDKH.to(
                    alkalinityMeasureCode,
                    it
                ).result
            }
        )

    fun from(
        plant: Plant,
        alkalinityMeasureCode: Int,
        temperatureMeasureCode: Int
    ): Plant =
        plant.copy(
            minTemperature = plant.minTemperature?.let {
                convertCelsius.from(
                    temperatureMeasureCode,
                    it
                ).result
            },
            maxTemperature = plant.maxTemperature?.let {
                convertCelsius.from(
                    temperatureMeasureCode,
                    it
                ).result
            },
            minPh = plant.minPh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxPh = plant.maxPh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minGh = plant.minGh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxGh = plant.maxGh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            minKh = plant.minKh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            },
            maxKh = plant.maxKh?.let {
                convertDKH.from(
                    alkalinityMeasureCode,
                    it
                ).result
            }
        )

}