package com.danilp.professionalaquarist.domain.plant

interface PlantDataSource {
    suspend fun insertPlant(plant: Plant)
    suspend fun getPlantById(id: Long): Plant?
    suspend fun getAllPlantsByAquarium(aquariumId: Long): List<Plant>
    suspend fun deletePlantById(id: Long)
}