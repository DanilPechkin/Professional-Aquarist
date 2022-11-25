package com.danilp.professionalaquarist.data.aquarium

import com.danilp.professionalaquarist.data.dweller.toDweller
import com.danilp.professionalaquarist.data.plant.toPlant
import com.danilp.professionalaquarist.database.AquariumDatabase
import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource
import com.danilp.professionalaquarist.domain.aquarium.ComfortTags
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerStatusTags
import com.danilp.professionalaquarist.domain.dweller.tags.DwellerTags
import com.danilp.professionalaquarist.domain.plant.tags.PlantStatusTags
import com.danilp.professionalaquarist.domain.plant.tags.PlantTags

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
                            tag == DwellerTags.FAST_CURRENT.code ||
                                    tag == DwellerTags.SLOW_CURRENT.code ||
                                    tag == DwellerTags.MEDIUM_CURRENT.code ||
                                    tag == DwellerTags.VEIL_TAILED.code ||
                                    tag == DwellerTags.BRIGHT_LIGHT.code ||
                                    tag == DwellerTags.LOW_LIGHT.code ||
                                    tag == DwellerTags.PLANT_LOVER.code ||
                                    tag == DwellerTags.NEEDS_SHELTER.code ||
                                    tag == DwellerTags.NEEDS_DRIFTWOOD.code ||
                                    tag == DwellerTags.BROADLEAF_PLANT.code ||
                                    tag == DwellerTags.LONG_STEMMED_PLANT.code ||
                                    tag == DwellerTags.FLOATING_PLANT.code ||
                                    tag == DwellerTags.MOSS.code
                        }?.map { tag ->
                            when (tag) {
                                DwellerTags.MOSS.code -> PlantTags.MOSS.code
                                DwellerTags.FLOATING_PLANT.code -> PlantTags.FLOATING_PLANT.code
                                DwellerTags.LONG_STEMMED_PLANT.code -> PlantTags.LONG_STEMMED_PLANT.code
                                DwellerTags.BROADLEAF_PLANT.code -> PlantTags.BROADLEAF_PLANT.code
                                else -> tag
                            }
                        }
                    }.flatten() + plants.mapNotNull {
                        it.tags?.filter { tag ->
                            tag == PlantTags.LOW_LIGHT.code ||
                                    tag == PlantTags.BRIGHT_LIGHT.code
                        }
                    }.flatten()).ifEmpty { null },
                    currentTags = ((aquarium.currentTags ?: emptyList()) +
                            plants.mapNotNull {
                                it.tags?.filter { tag ->
                                    tag == PlantTags.BROADLEAF_PLANT.code ||
                                            tag == PlantTags.LONG_STEMMED_PLANT.code ||
                                            tag == PlantTags.FLOATING_PLANT.code ||
                                            tag == PlantTags.MOSS.code ||
                                            tag == PlantTags.FERN.code
                                }
                            }.flatten() +
                            if (plants.isNotEmpty())
                                listOf(DwellerTags.PLANT_LOVER.code)
                            else listOf()
                            ).ifEmpty { null },
                    minTemperature = (dwellers.mapNotNull { it.minTemperature } +
                            plants.mapNotNull { it.minTemperature }).maxOrNull(),
                    maxTemperature = (dwellers.mapNotNull { it.maxTemperature } +
                            plants.mapNotNull { it.maxTemperature }).minOrNull(),
                    minPh = (dwellers.mapNotNull { it.minPh } +
                            plants.mapNotNull { it.minPh }).maxOrNull(),
                    maxPh = (dwellers.mapNotNull { it.maxPh } +
                            plants.mapNotNull { it.maxPh }).minOrNull(),
                    minGh = (dwellers.mapNotNull { it.minGh } +
                            plants.mapNotNull { it.minGh }).maxOrNull(),
                    maxGh = (dwellers.mapNotNull { it.maxGh } +
                            plants.mapNotNull { it.maxGh }).minOrNull(),
                    minKh = (dwellers.mapNotNull { it.minKh } +
                            plants.mapNotNull { it.minKh }).maxOrNull(),
                    maxKh = (dwellers.mapNotNull { it.maxKh } +
                            plants.mapNotNull { it.maxKh }).minOrNull(),
                    minIllumination = plants.mapNotNull { it.minIllumination }.maxOrNull(),
                    minCO2 = plants.mapNotNull { it.minCO2 }.maxOrNull()
                )
            )

            val isAquariumCapacityEnough =
                (aquarium.liters ?: 0.0) >= dwellers.sumOf {
                    (it.liters ?: 0.0) * (it.amount ?: 0)
                }

            dwellers.forEach { dweller ->
                val isWaterParametersMatch =
                    (dweller.minTemperature ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentTemperature ?: dweller.minTemperature ?: 0.0) &&
                            (dweller.maxTemperature ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentTemperature ?: dweller.maxTemperature ?: 0.0) &&
                            (dweller.minPh ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentPh ?: dweller.minPh ?: 0.0) &&
                            (dweller.maxPh ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentPh ?: dweller.maxPh ?: 0.0) &&
                            (dweller.minGh ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentGh ?: dweller.minGh ?: 0.0) &&
                            (dweller.maxGh ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentGh ?: dweller.maxGh ?: 0.0) &&
                            (dweller.minKh ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentKh ?: dweller.minKh ?: 0.0) &&
                            (dweller.maxKh ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentKh ?: dweller.maxKh ?: 0.0)
                val notMetTags = dweller.tags?.filter { tag ->
                    listOf(
                        DwellerTags.FAST_CURRENT.code,
                        DwellerTags.SLOW_CURRENT.code,
                        DwellerTags.MEDIUM_CURRENT.code,
                        DwellerTags.VEIL_TAILED.code,
                        DwellerTags.BRIGHT_LIGHT.code,
                        DwellerTags.LOW_LIGHT.code,
                        DwellerTags.PLANT_LOVER.code,
                        DwellerTags.NEEDS_SHELTER.code,
                        DwellerTags.BROADLEAF_PLANT.code,
                        DwellerTags.LONG_STEMMED_PLANT.code,
                        DwellerTags.FLOATING_PLANT.code,
                        DwellerTags.MOSS.code,
                        DwellerTags.NEEDS_DRIFTWOOD.code
                    ).contains(tag)
                }?.map { tag ->
                    when (tag) {
                        DwellerTags.MOSS.code -> PlantTags.MOSS.code
                        DwellerTags.FLOATING_PLANT.code -> PlantTags.FLOATING_PLANT.code
                        DwellerTags.LONG_STEMMED_PLANT.code -> PlantTags.LONG_STEMMED_PLANT.code
                        DwellerTags.BROADLEAF_PLANT.code -> PlantTags.BROADLEAF_PLANT.code
                        else -> tag
                    }
                }?.filter { tag ->
                    !((aquarium.currentTags ?: emptyList()).contains(tag))
                } ?: emptyList()

                val statusTags = (
                        if (!isAquariumCapacityEnough) {
                            listOf(DwellerStatusTags.AQUARIUM_CAPACITY_NOT_MET.code)
                        } else {
                            listOf()
                        } +
                                if (!isWaterParametersMatch) {
                                    listOf(DwellerStatusTags.WATER_PARAMETERS_NOT_MET.code)
                                } else {
                                    listOf()
                                } +
                                if (notMetTags.isNotEmpty()) {
                                    listOf(DwellerStatusTags.TAGS_NOT_MET.code)
                                } else {
                                    listOf()
                                }
                        )

                val updatedDweller = dweller.copy(
                    statusTags = (statusTags + notMetTags).ifEmpty { null },
                    status = when (statusTags.size) {
                        0 -> ComfortTags.VERY_SATISFIED.code
                        1 -> ComfortTags.SATISFIED.code
                        2 -> ComfortTags.NEUTRAL.code
                        3 -> ComfortTags.DISSATISFIED.code
                        4 -> ComfortTags.VERY_DISSATISFIED.code
                        else -> ComfortTags.VERY_DISSATISFIED.code
                    }
                )
                dwellerQueries.insertDweller(
                    id = updatedDweller.id,
                    aquariumId = updatedDweller.aquariumId,
                    imageUrl = updatedDweller.imageUrl,
                    name = updatedDweller.name,
                    genus = updatedDweller.genus,
                    amount = updatedDweller.amount,
                    description = updatedDweller.description,
                    tags = updatedDweller.tags?.joinToString(" "),
                    liters = updatedDweller.liters,
                    minTemperature = updatedDweller.minTemperature,
                    maxTemperature = updatedDweller.maxTemperature,
                    minPh = updatedDweller.minPh,
                    maxPh = updatedDweller.maxPh,
                    minGh = updatedDweller.minGh,
                    maxGh = updatedDweller.maxGh,
                    minKh = updatedDweller.minKh,
                    maxKh = updatedDweller.maxKh,
                    statusTags = updatedDweller.statusTags?.joinToString(" "),
                    status = updatedDweller.status
                )
            }

            plants.forEach { plant ->
                val isWaterParametersMatch =
                    (plant.minTemperature ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentTemperature ?: plant.minTemperature ?: 0.0) &&
                            (plant.maxTemperature ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentTemperature ?: plant.maxTemperature ?: 0.0) &&
                            (plant.minPh ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentPh ?: plant.minPh ?: 0.0) &&
                            (plant.maxPh ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentPh ?: plant.maxPh ?: 0.0) &&
                            (plant.minGh ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentGh ?: plant.minGh ?: 0.0) &&
                            (plant.maxGh ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentGh ?: plant.maxGh ?: 0.0) &&
                            (plant.minKh ?: aquarium.currentTemperature ?: 0.0) <=
                            (aquarium.currentKh ?: plant.minKh ?: 0.0) &&
                            (plant.maxKh ?: aquarium.currentTemperature ?: 0.0) >=
                            (aquarium.currentKh ?: plant.maxKh ?: 0.0) &&
                            (plant.minCO2 ?: aquarium.currentCO2 ?: 0.0) <=
                            (aquarium.currentCO2 ?: plant.minCO2 ?: 0.0)
                val notMetTags = plant.tags?.filter { tag ->
                    listOf(
                        PlantTags.LOW_LIGHT.code,
                        PlantTags.BRIGHT_LIGHT.code
                    ).contains(tag)
                }?.filter { tag ->
                    !((aquarium.currentTags ?: emptyList()).contains(tag))
                } ?: emptyList()

                val isEnoughIllumination = (aquarium.currentIllumination ?: 0.0) >=
                        (plant.minIllumination ?: 0.0)

                val isInDanger =
                    (aquarium.currentTags?.contains(DwellerTags.PLANT_EATER.code)) ?: false

                val statusTags =
                    if (!isEnoughIllumination) {
                        listOf(PlantStatusTags.NOT_ENOUGH_ILLUMINATION.code)
                    } else {
                        listOf()
                    } +
                            if (!isWaterParametersMatch) {
                                listOf(PlantStatusTags.WATER_PARAMETERS_NOT_MET.code)
                            } else {
                                listOf()
                            } +
                            if (notMetTags.isNotEmpty()) {
                                listOf(PlantStatusTags.TAGS_NOT_MET.code)
                            } else {
                                listOf()
                            } +
                            if (isInDanger) {
                                listOf(PlantStatusTags.IN_DANGER.code)
                            } else {
                                listOf()
                            }


                val updatedPlant = plant.copy(
                    statusTags = (statusTags + notMetTags).ifEmpty { null },
                    status = when (statusTags.size) {
                        0 -> ComfortTags.VERY_SATISFIED.code
                        1 -> ComfortTags.SATISFIED.code
                        2 -> ComfortTags.NEUTRAL.code
                        3 -> ComfortTags.DISSATISFIED.code
                        4 -> ComfortTags.VERY_DISSATISFIED.code
                        else -> ComfortTags.VERY_DISSATISFIED.code
                    }
                )
                plantQueries.insertPlant(
                    id = updatedPlant.id,
                    aquariumId = updatedPlant.aquariumId,
                    imageUrl = updatedPlant.imageUrl,
                    name = updatedPlant.name,
                    genus = updatedPlant.genus,
                    description = updatedPlant.description,
                    tags = updatedPlant.tags?.joinToString(" "),
                    minTemperature = updatedPlant.minTemperature,
                    maxTemperature = updatedPlant.maxTemperature,
                    minPh = updatedPlant.minPh,
                    maxPh = updatedPlant.maxPh,
                    minGh = updatedPlant.minGh,
                    maxGh = updatedPlant.maxGh,
                    minKh = updatedPlant.minKh,
                    maxKh = updatedPlant.maxKh,
                    minIllumination = updatedPlant.minIllumination,
                    minCO2 = updatedPlant.minCO2,
                    statusTags = updatedPlant.statusTags?.joinToString(" "),
                    status = updatedPlant.status
                )
            }
        }
    }
}