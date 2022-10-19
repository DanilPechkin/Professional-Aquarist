package com.danilp.professionalaquarist.domain.aquarium

class SearchAquariums {
    fun execute(aquariums: List<Aquarium>, query: String): List<Aquarium> =
        if (query.isBlank())
            aquariums
        else
            aquariums.filter {
                it.name!!.trim().lowercase().contains(query.lowercase())
            }
}