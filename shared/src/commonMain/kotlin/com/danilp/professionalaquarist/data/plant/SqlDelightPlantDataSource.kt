package com.danilp.professionalaquarist.data.plant

import com.danilp.professionalaquarist.database.AquariumDatabase
import com.danilp.professionalaquarist.domain.plant.Plant
import com.danilp.professionalaquarist.domain.plant.PlantDataSource

class SqlDelightPlantDataSource(db: AquariumDatabase) : PlantDataSource {

    private val queries = db.plantQueries

    override suspend fun insertPlant(plant: Plant) {
        queries.insertPlant(
            id = plant.id,
            aquariumId = plant.aquariumId,
            imageUrl = plant.imageUrl,
            name = plant.name,
            genus = plant.genus,
            description = plant.description,
            minTemperature = plant.minTemperature,
            maxTemperature = plant.maxTemperature,
            minPh = plant.minPh,
            maxPh = plant.maxPh,
            minGh = plant.minGh,
            maxGh = plant.maxGh,
            minKh = plant.minKh,
            maxKh = plant.maxKh,
            minCO2 = plant.minCO2,
            minIllumination = plant.minIllumination
        )
    }

    override suspend fun getPlantById(id: Long): Plant? =
        queries
            .getPlantById(id)
            .executeAsOneOrNull()
            ?.toPlant()

    override suspend fun getAllPlantsByAquarium(aquariumId: Long): List<Plant> =
        queries
            .getAllPlantsByAquarium(aquariumId)
            .executeAsList()
            .map { it.toPlant() }


    override suspend fun deletePlantById(id: Long) {
        queries.deletePlantById(id)
    }

}