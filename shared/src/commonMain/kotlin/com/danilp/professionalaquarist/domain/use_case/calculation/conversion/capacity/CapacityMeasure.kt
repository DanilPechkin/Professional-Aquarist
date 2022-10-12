package com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity

enum class CapacityMeasure(val code: Int) {
    Liters(0),
    CubicFeet(1),
    USCups(2),
    Teaspoons(3),
    Tablespoons(4),
    Milliliters(5),
    MetricCups(6),
    Gallons(7),
    CubicMeters(8),
    CubicInches(9),
    CubicCentimeters(10)
}