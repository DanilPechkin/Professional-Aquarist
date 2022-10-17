package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.list

import com.danilp.professionalaquarist.domain.plant.Plant

data class PlantsListState(
    val plants: List<Plant> = emptyList(),
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val aquariumId: Long = 0
)
