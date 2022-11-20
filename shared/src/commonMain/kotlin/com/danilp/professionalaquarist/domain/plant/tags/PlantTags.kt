package com.danilp.professionalaquarist.domain.plant.tags

enum class PlantTags(val code: String) {

    BROADLEAF_PLANT("Broadleaf"), // любит широколистные растения
    LONG_STEMMED_PLANT("Long-stemmed"), // любит длинностебельные растения
    FLOATING_PLANT("Floating"), // любит плавающие растения
    MOSS("Moss"), // мох
    FERN("Fern"), // папоротник

    BRIGHT_LIGHT("Bright_light"), // любит яркий свет
    LOW_LIGHT("Low_light") // любит слабый свет

}