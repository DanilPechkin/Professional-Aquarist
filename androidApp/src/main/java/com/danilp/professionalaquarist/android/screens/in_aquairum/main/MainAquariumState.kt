package com.danilp.professionalaquarist.android.screens.in_aquairum.main

import com.danilp.professionalaquarist.domain.aquarium.Aquarium

data class MainAquariumState(
    val aquarium: Aquarium = Aquarium.createEmpty()
)
