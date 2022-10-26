package com.danilp.professionalaquarist.android.screens.in_aquairum.plant.edit

sealed interface PlantEditEvent {
    object DeleteButtonPressed : PlantEditEvent
    object InsertButtonPressed : PlantEditEvent
    data class NameChanged(val name: String) : PlantEditEvent
    data class GenusChanged(val genus: String) : PlantEditEvent
    data class MinTemperatureChanged(val temp: String) : PlantEditEvent
    data class MaxTemperatureChanged(val temp: String) : PlantEditEvent
    data class MinPhChanged(val ph: String) : PlantEditEvent
    data class MaxPhChanged(val ph: String) : PlantEditEvent
    data class MinGhChanged(val gh: String) : PlantEditEvent
    data class MaxGhChanged(val gh: String) : PlantEditEvent
    data class MinKhChanged(val kh: String) : PlantEditEvent
    data class MaxKhChanged(val kh: String) : PlantEditEvent
    data class MinCO2Changed(val co2: String) : PlantEditEvent
    data class MinIlluminationChanged(val illumination: String) : PlantEditEvent
    data class DescriptionChanged(val description: String) : PlantEditEvent
    data class ImagePicked(val imageUrl: String) : PlantEditEvent
}
