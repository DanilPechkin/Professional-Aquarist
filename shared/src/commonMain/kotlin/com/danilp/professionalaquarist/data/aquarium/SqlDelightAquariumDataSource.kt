package com.danilp.professionalaquarist.data.aquarium

import com.danilp.professionalaquarist.data.dweller.toDweller
import com.danilp.professionalaquarist.data.plant.toPlant
import com.danilp.professionalaquarist.database.AquariumDatabase
import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.dweller.DwellerTags
import com.danilp.professionalaquarist.domain.plant.PlantTags

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
            currentTags = aquarium.currentTags?.joinToString(" "),
            requiredTags = aquarium.requiredTags?.joinToString(" "),
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
                    requiredTags = (dwellers.mapNotNull {
                        it.tags?.filter { tag ->
                            tag == DwellerTags.FAST_CURRENT ||
                                    tag == DwellerTags.SLOW_CURRENT ||
                                    tag == DwellerTags.MEDIUM_CURRENT ||
                                    tag == DwellerTags.VEIL_TAILED ||
                                    tag == DwellerTags.BRIGHT_LIGHT ||
                                    tag == DwellerTags.LOW_LIGHT ||
                                    tag == DwellerTags.PLANT_LOVER ||
                                    tag == DwellerTags.NEEDS_SHELTER ||
                                    tag == DwellerTags.BROADLEAF_PLANT ||
                                    tag == DwellerTags.LONG_STEMMED_PLANT ||
                                    tag == DwellerTags.FLOATING_PLANT ||
                                    tag == DwellerTags.MOSS
                        }
                    }.flatten() + plants.mapNotNull {
                        it.tags?.filter { tag ->
                            tag == PlantTags.LOW_LIGHT ||
                                    tag == PlantTags.BRIGHT_LIGHT
                        }
                    }.flatten()).ifEmpty { null },
                    currentTags = ((aquarium.currentTags ?: emptyList()) +
                            plants.mapNotNull {
                                it.tags?.filter { tag ->
                                    tag == PlantTags.BROADLEAF_PLANT ||
                                            tag == PlantTags.LONG_STEMMED_PLANT ||
                                            tag == PlantTags.FLOATING_PLANT ||
                                            tag == PlantTags.MOSS ||
                                            tag == PlantTags.FERN
                                }
                            }.flatten()).ifEmpty { null },
                    minTemperature = listOf(
                        dwellers.mapNotNull { it.minTemperature }.maxOrNull(),
                        plants.mapNotNull { it.minTemperature }.maxOrNull()
                    ).mapNotNull { it }.maxOrNull(),
                    maxTemperature = listOf(
                        dwellers.mapNotNull { it.maxTemperature }.minOrNull(),
                        plants.mapNotNull { it.maxTemperature }.minOrNull()
                    ).mapNotNull { it }.minOrNull(),
                    minPh = listOf(
                        dwellers.mapNotNull { it.minPh }.maxOrNull(),
                        plants.mapNotNull { it.minPh }.maxOrNull()
                    ).mapNotNull { it }.maxOrNull(),
                    maxPh = listOf(
                        dwellers.mapNotNull { it.maxPh }.minOrNull(),
                        plants.mapNotNull { it.maxPh }.minOrNull()
                    ).mapNotNull { it }.minOrNull(),
                    minGh = listOf(
                        dwellers.mapNotNull { it.minGh }.maxOrNull(),
                        plants.mapNotNull { it.minGh }.maxOrNull()
                    ).mapNotNull { it }.maxOrNull(),
                    maxGh = listOf(
                        dwellers.mapNotNull { it.maxGh }.minOrNull(),
                        plants.mapNotNull { it.maxGh }.minOrNull()
                    ).mapNotNull { it }.minOrNull(),
                    minKh = listOf(
                        dwellers.mapNotNull { it.minKh }.maxOrNull(),
                        plants.mapNotNull { it.minKh }.maxOrNull()
                    ).mapNotNull { it }.maxOrNull(),
                    maxKh = listOf(
                        dwellers.mapNotNull { it.maxKh }.minOrNull(),
                        plants.mapNotNull { it.maxKh }.minOrNull()
                    ).mapNotNull { it }.minOrNull(),
                    minIllumination = plants.mapNotNull { it.minIllumination }.maxOrNull(),
                    minCO2 = plants.mapNotNull { it.minCO2 }.maxOrNull()
                )
            )
        }
    }
}