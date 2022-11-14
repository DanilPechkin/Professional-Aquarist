package com.danilp.professionalaquarist.domain.plant.use_case

import com.danilp.professionalaquarist.domain.plant.Plant

class SearchPlants {
    fun execute(plants: List<Plant>, query: String): List<Plant> =
        if (query.isBlank())
            plants
        else
            plants.filter {
                it.name!!.trim().lowercase().contains(query.lowercase())
            }
}