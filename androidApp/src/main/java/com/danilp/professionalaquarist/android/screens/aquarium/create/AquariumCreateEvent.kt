package com.danilp.professionalaquarist.android.screens.aquarium.create

sealed interface AquariumCreateEvent {
    object InsertButtonPressed : AquariumCreateEvent
    data class NameChanged(val name: String) : AquariumCreateEvent
    data class LitersChanged(val liters: String) : AquariumCreateEvent
    data class DescriptionChanged(val description: String) : AquariumCreateEvent
    data class CurrentIlluminationChanged(val currentIllumination: String) : AquariumCreateEvent
    data class ImagePicked(val imageUri: String) : AquariumCreateEvent
}
