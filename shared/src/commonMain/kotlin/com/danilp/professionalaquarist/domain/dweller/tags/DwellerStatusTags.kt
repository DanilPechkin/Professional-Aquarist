package com.danilp.professionalaquarist.domain.dweller.tags

enum class DwellerStatusTags(val code: String) {
    WATER_PARAMETERS_NOT_MET("water_parameters_not_met"),
    TAGS_NOT_MET("tags_not_met"),
    AQUARIUM_CAPACITY_NOT_MET("aquarium_capacity_not_met"),
    IN_DANGER("in_danger")
}