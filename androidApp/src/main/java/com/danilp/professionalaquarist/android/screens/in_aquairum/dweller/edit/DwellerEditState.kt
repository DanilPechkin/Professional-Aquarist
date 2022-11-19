package com.danilp.professionalaquarist.android.screens.in_aquairum.dweller.edit

import com.danilp.professionalaquarist.domain.dweller.Dweller
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.alkalinity.AlkalinityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.capacity.CapacityMeasure
import com.danilp.professionalaquarist.domain.use_case.calculation.conversion.temperature.TemperatureMeasure

data class DwellerEditState(
    val dweller: Dweller = Dweller.createEmpty(),
    val isLoading: Boolean = false,
    val temperatureMeasureCode: Int = TemperatureMeasure.Celsius.code,
    val alkalinityMeasureCode: Int = AlkalinityMeasure.DKH.code,
    val capacityMeasureCode: Int = CapacityMeasure.Liters.code,
    // Stats
    val name: String = "",
    val genus: String = "",
    val amount: String = "",
    val minTemperature: String = "",
    val maxTemperature: String = "",
    val liters: String = "",
    val minPh: String = "",
    val maxPh: String = "",
    val minGh: String = "",
    val maxGh: String = "",
    val minKh: String = "",
    val maxKh: String = "",
    val description: String = "",
    // Stats errors
    val nameErrorCode: Int? = null,
    /*TODO: val genusError: Int? = null,*/
    val amountErrorCode: Int? = null,
    val minTemperatureErrorCode: Int? = null,
    val maxTemperatureErrorCode: Int? = null,
    val litersErrorCode: Int? = null,
    val minPhErrorCode: Int? = null,
    val maxPhErrorCode: Int? = null,
    val minGhErrorCode: Int? = null,
    val maxGhErrorCode: Int? = null,
    val minKhErrorCode: Int? = null,
    val maxKhErrorCode: Int? = null
)
