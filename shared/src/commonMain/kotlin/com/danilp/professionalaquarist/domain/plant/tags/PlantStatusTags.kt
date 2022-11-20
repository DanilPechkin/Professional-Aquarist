package com.danilp.professionalaquarist.domain.plant.tags

enum class PlantStatusTags(val code: String) {
    WATER_PARAMETERS_NOT_MET("water_parameters_not_met"),
    TAGS_NOT_MET("tags_not_met"),
    IN_DANGER("in_danger"),
    NOT_ENOUGH_ILLUMINATION("not_enough_illumination")
}