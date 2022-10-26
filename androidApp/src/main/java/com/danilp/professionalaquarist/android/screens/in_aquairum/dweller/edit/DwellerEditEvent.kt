package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.edit

sealed interface DwellerEditEvent {
    object DeleteButtonPressed : DwellerEditEvent
    object InsertButtonPressed : DwellerEditEvent
    data class NameChanged(val name: String) : DwellerEditEvent
    data class GenusChanged(val genus: String) : DwellerEditEvent
    data class AmountChanged(val amount: String) : DwellerEditEvent
    data class MinTemperatureChanged(val temp: String) : DwellerEditEvent
    data class MaxTemperatureChanged(val temp: String) : DwellerEditEvent
    data class LitersChanged(val liters: String) : DwellerEditEvent
    data class MinPhChanged(val ph: String) : DwellerEditEvent
    data class MaxPhChanged(val ph: String) : DwellerEditEvent
    data class MinGhChanged(val gh: String) : DwellerEditEvent
    data class MaxGhChanged(val gh: String) : DwellerEditEvent
    data class MinKhChanged(val kh: String) : DwellerEditEvent
    data class MaxKhChanged(val kh: String) : DwellerEditEvent
    data class DescriptionChanged(val description: String) : DwellerEditEvent
    data class ImagePicked(val imageUrl: String) : DwellerEditEvent
}
