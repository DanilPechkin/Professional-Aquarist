package com.danilp.professionalaquarist.data.dweller

import com.danilp.professionalaquarist.database.AquariumDatabase
import com.danilp.professionalaquarist.domain.dweller.Dweller
import com.danilp.professionalaquarist.domain.dweller.DwellerDataSource

class SqlDelightDwellerDataSource(db: AquariumDatabase) : DwellerDataSource {

    private val queries = db.dwellerQueries

    override suspend fun insertDweller(dweller: Dweller) {
        queries.insertDweller(
            id = dweller.id,
            aquariumId = dweller.aquariumId,
            imageUrl = dweller.imageUrl,
            name = dweller.name,
            genus = dweller.genus,
            amount = dweller.amount,
            description = dweller.description,
            tags = dweller.tags?.joinToString(" "),
            liters = dweller.liters,
            minTemperature = dweller.minTemperature,
            maxTemperature = dweller.maxTemperature,
            minPh = dweller.minPh,
            maxPh = dweller.maxPh,
            minGh = dweller.minGh,
            maxGh = dweller.maxGh,
            minKh = dweller.minKh,
            maxKh = dweller.maxKh
        )
    }

    override suspend fun getDwellerById(id: Long): Dweller? =
        queries
            .getDwellerById(id)
            .executeAsOneOrNull()
            ?.toDweller()


    override suspend fun getAllDwellersByAquarium(aquariumId: Long): List<Dweller> =
        queries
            .getAllDwellersByAquarium(aquariumId)
            .executeAsList()
            .map { it.toDweller() }

    override suspend fun deleteDwellerById(id: Long) {
        queries.deleteDwellerById(id)
    }
}