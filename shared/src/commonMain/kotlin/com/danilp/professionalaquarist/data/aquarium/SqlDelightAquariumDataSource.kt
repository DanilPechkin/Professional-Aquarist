package com.danilp.professionalaquarist.data.aquarium

import com.danilp.professionalaquarist.data.dweller.toDweller
import com.danilp.professionalaquarist.data.plant.toPlant
import com.danilp.professionalaquarist.database.AquariumDatabase
import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import kotlin.math.max
import kotlin.math.min

class SqlDelightAquariumDataSource(db: AquariumDatabase) : AquariumDataSource {

    private val aquariumQueries = db.aquariumQueries
    private val dwellerQueries = db.dwellerQueries
    private val plantQueries = db.plantQueries

    override suspend fun insertAquarium(aquarium: Aquarium) {
        aquariumQueries.insertAquarium(
            id = aquarium.id,
            imageUrl = aquarium.imageUrl,
            name = aquarium.name,
            description = aquarium.description,
            liters = aquarium.liters,
            minIllumination = aquarium.minIllumination,
            currentIllumination = aquarium.currentIllumination,
            currentCO2 = aquarium.currentCO2,
            minCO2 = aquarium.minCO2,
            currentTemperature = aquarium.currentTemperature,
            minTemperature = aquarium.minTemperature,
            maxTemperature = aquarium.maxTemperature,
            currentPh = aquarium.currentPh,
            minPh = aquarium.minPh,
            maxPh = aquarium.maxPh,
            currentGh = aquarium.currentGh,
            minGh = aquarium.minGh,
            maxGh = aquarium.maxGh,
            currentKh = aquarium.currentKh,
            minKh = aquarium.minKh,
            maxKh = aquarium.maxKh,
            currentK = aquarium.currentK,
            minK = aquarium.minK,
            maxK = aquarium.maxK,
            currentNO3 = aquarium.currentNO3,
            minNO3 = aquarium.minNO3,
            maxNO3 = aquarium.maxNO3,
            currentFe = aquarium.currentFe,
            minFe = aquarium.minFe,
            maxFe = aquarium.maxFe,
            currentCa = aquarium.currentCa,
            minCa = aquarium.minCa,
            maxCa = aquarium.maxCa,
            currentMg = aquarium.currentMg,
            minMg = aquarium.minMg,
            maxMg = aquarium.maxMg,
            currentPO4 = aquarium.currentPO4,
            minPO4 = aquarium.minPO4,
            maxPO4 = aquarium.maxPO4,
            currentAmmonia = aquarium.currentAmmonia,
            minAmmonia = aquarium.minAmmonia,
            maxAmmonia = aquarium.maxAmmonia
        )
    }

    override suspend fun getAquariumById(id: Long): Aquarium? =
        aquariumQueries
            .getAquariumById(id)
            .executeAsOneOrNull()
            ?.toAquarium()

    override suspend fun getAllAquariums(): List<Aquarium> =
        aquariumQueries
            .getAllAquariums()
            .executeAsList()
            .map { it.toAquarium() }

    override suspend fun deleteAquariumById(id: Long) {
        aquariumQueries.deleteAquariumById(id)
    }

    override suspend fun refreshAquariumById(id: Long) {
        val dwellers =
            dwellerQueries.getAllDwellersByAquarium(id).executeAsList().map { it.toDweller() }
        val plants = plantQueries.getAllPlantsByAquarium(id).executeAsList().map { it.toPlant() }
        getAquariumById(id)?.let { aquarium ->
            insertAquarium(
                aquarium.copy(
                    minTemperature = max(
                        dwellers.maxOfOrNull { it.minTemperature } ?: aquarium.minTemperature,
                        plants.maxOfOrNull { it.minTemperature } ?: aquarium.minTemperature
                    ),
                    maxTemperature = min(
                        dwellers.minOfOrNull { it.maxTemperature } ?: aquarium.maxTemperature,
                        plants.minOfOrNull { it.maxTemperature } ?: aquarium.maxTemperature
                    ),
                    minPh = max(
                        dwellers.maxOfOrNull { it.minPh } ?: aquarium.minPh,
                        plants.maxOfOrNull { it.minPh } ?: aquarium.minPh
                    ),
                    maxPh = min(
                        dwellers.minOfOrNull { it.maxPh } ?: aquarium.maxPh,
                        plants.minOfOrNull { it.maxPh } ?: aquarium.maxPh
                    ),
                    minGh = max(
                        dwellers.maxOfOrNull { it.minGh } ?: aquarium.minGh,
                        plants.maxOfOrNull { it.minGh } ?: aquarium.minGh
                    ),
                    maxGh = min(
                        dwellers.minOfOrNull { it.maxGh } ?: aquarium.maxGh,
                        plants.minOfOrNull { it.maxGh } ?: aquarium.maxGh
                    ),
                    minKh = max(
                        dwellers.maxOfOrNull { it.minKh } ?: aquarium.minKh,
                        plants.maxOfOrNull { it.minKh } ?: aquarium.minKh
                    ),
                    maxKh = min(
                        dwellers.minOfOrNull { it.maxKh } ?: aquarium.maxKh,
                        plants.minOfOrNull { it.maxKh } ?: aquarium.maxKh
                    ),
                    minIllumination = plants.maxOfOrNull { it.minIllumination }
                        ?: aquarium.minIllumination,
                    minCO2 = plants.maxOfOrNull { it.minCO2 } ?: aquarium.minCO2
                )
            )
        }
    }
}