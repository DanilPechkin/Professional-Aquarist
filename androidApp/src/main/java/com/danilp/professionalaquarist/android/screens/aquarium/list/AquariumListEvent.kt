package com.danilp.professionalaquarist.android.screens.aquarium.list

sealed class AquariumListEvent {
    object Refresh : AquariumListEvent()
    data class OnSearchQueryChange(val query: String) : AquariumListEvent()
    data class OnAquariumClicked(val aquariumId: Long) : AquariumListEvent()
}
