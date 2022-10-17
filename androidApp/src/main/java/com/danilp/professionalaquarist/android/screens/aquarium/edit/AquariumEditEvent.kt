package com.danilp.professionalaquarist.android.screens.aquarium.edit

sealed class AquariumEditEvent {
    object DeleteButtonPressed : AquariumEditEvent()
    object InsertButtonPressed : AquariumEditEvent()
    data class NameChanged(val name: String) : AquariumEditEvent()
    data class LitersChanged(val liters: String) : AquariumEditEvent()
    data class DescriptionChanged(val description: String) : AquariumEditEvent()
    data class ImagePicked(val imageUri: String) : AquariumEditEvent()
}