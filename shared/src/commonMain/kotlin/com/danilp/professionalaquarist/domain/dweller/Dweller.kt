package com.danilp.professionalaquarist.domain.dweller

data class Dweller(
    var id: Long?,
    val aquariumId: Long,
    val imageUrl: String?,
    val name: String?,
    val genus: String?,
    val amount: Long?,
    val minTemperature: Double?,
    val maxTemperature: Double?,
    val liters: Double?,
    val minPh: Double?,
    val maxPh: Double?,
    val minGh: Double?,
    val maxGh: Double?,
    val minKh: Double?,
    val maxKh: Double?,
    val description: String?,
    val tags: List<String>?
) {
    companion object {
        fun createEmpty(): Dweller = Dweller(
            null,
            0,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    }
}