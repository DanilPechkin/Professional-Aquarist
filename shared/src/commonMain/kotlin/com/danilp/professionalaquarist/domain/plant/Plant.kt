package com.danilp.professionalaquarist.domain.plant

data class Plant(
    val id: Long?,
    val aquariumId: Long,
    val imageUrl: String,
    val name: String,
    val genus: String,
    val minTemperature: Double,
    val maxTemperature: Double,
    val minPh: Double,
    val maxPh: Double,
    val minGh: Double,
    val maxGh: Double,
    val minKh: Double,
    val maxKh: Double,
    val minCO2: Double,
    val minIllumination: Double,
    val description: String
) {
    companion object {
        fun createEmpty(): Plant = Plant(
            null,
            0,
            "",
            "",
            "",
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            ""
        )
    }
}
