package com.danilp.professionalaquarist.data.aquarium

import com.danilp.professionalaquarist.database.AquariumDatabase
import com.danilp.professionalaquarist.domain.aquarium.Aquarium
import com.danilp.professionalaquarist.domain.aquarium.AquariumDataSource

class SqlDelightAquariumDataSource(db: AquariumDatabase) : AquariumDataSource {

    private val queries = db.aquariumQueries

    override suspend fun insertAquarium(aquarium: Aquarium) {
        queries.insertAquarium(
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
        queries
            .getAquariumById(id)
            .executeAsOneOrNull()
            ?.toAquarium()

    override suspend fun getAllAquariums(): List<Aquarium> =
        queries
            .getAllAquariums()
            .executeAsList()
            .map { it.toAquarium() }

    override suspend fun deleteAquariumById(id: Long) {
        queries.deleteAquariumById(id)
    }
}