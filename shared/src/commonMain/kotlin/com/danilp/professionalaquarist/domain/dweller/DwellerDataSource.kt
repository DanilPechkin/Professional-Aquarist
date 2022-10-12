package com.danilp.professionalaquarist.domain.dweller

interface DwellerDataSource {
    suspend fun insertDweller(dweller: Dweller)
    suspend fun getDwellerById(id: Long): Dweller?
    suspend fun getAllDwellersByAquarium(aquariumId: Long): List<Dweller>
    suspend fun deleteDwellerById(id: Long)
}