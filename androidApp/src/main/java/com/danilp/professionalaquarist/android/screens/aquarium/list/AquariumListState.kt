package com.danilp.professionalaquarist.android.screens.aquarium.list

import com.danilp.professionalaquarist.domain.aquarium.Aquarium

data class AquariumListState(
    val aquariums: List<Aquarium> = emptyList(),
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)
