package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.info

import com.danilp.professionalaquarist.domain.dweller.Dweller
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class DwellerInfoState(
    val dweller: Dweller = Dweller.createEmpty(),
    val temperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val alkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
)
