package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.list

import com.danilp.professionalaquarist.domain.dweller.Dweller

data class DwellersListState(
    val dwellers: List<Dweller> = emptyList(),
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val aquariumId: Long = 0
)
