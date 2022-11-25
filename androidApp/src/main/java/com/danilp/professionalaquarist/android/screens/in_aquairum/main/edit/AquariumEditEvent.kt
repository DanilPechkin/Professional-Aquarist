package com.danilp.professionalaquarist.android.screens.in_aquairum.main.edit

sealed interface AquariumEditEvent {
    object DeleteButtonPressed : AquariumEditEvent
    object InsertButtonPressed : AquariumEditEvent
    data class NameChanged(val name: String) : AquariumEditEvent
    data class LitersChanged(val liters: String) : AquariumEditEvent
    data class DescriptionChanged(val description: String) : AquariumEditEvent
    data class ImagePicked(val imageUri: String) : AquariumEditEvent
    data class CurrentTagSelected(val tag: String) : AquariumEditEvent
}
