package com.danilp.professionalaquarist.domain.aquarium

interface AquariumDataSource {
    suspend fun insertAquarium(aquarium: Aquarium)
    suspend fun getAquariumById(id: Long): Aquarium?
    suspend fun getAllAquariums(): List<Aquarium>
    suspend fun deleteAquariumById(id: Long)
    suspend fun refreshAquariumById(id: Long)
}